package com.bugalu.orchestrator.service;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bugalu.domain.stock.StockDocument;
import com.bugalu.domain.stock.StockDto;
import com.bugalu.domain.twitter.Post;
import com.bugalu.domain.twitter.SocialMedia;
import com.bugalu.domain.twitter.Stats;
import com.bugalu.domain.twitter.Twit;
import com.bugalu.domain.utils.AppConstants;

@Component
public class OrchestratorService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final ConcurrentRestClient service;
	final private ConcurrentLinkedQueue<Future<SocialMedia>> socialMediaQueue;
	final private PriorityQueue<Post> postQueue;
	final private Map<String, Stats> statsMap;
	private final KafkaTemplate<String, StockDocument> kafkaTemplate;

	@Autowired
	public OrchestratorService(ConcurrentRestClient service, KafkaTemplate<String, StockDocument> kafkaTemplate) {
		this.service = service;
		this.kafkaTemplate = kafkaTemplate;
		postQueue = new PriorityQueue<>(Comparator.comparing(Post::getCreatedDate));
		socialMediaQueue = new ConcurrentLinkedQueue<>();
		statsMap = new ConcurrentHashMap<>();
	}

	@Scheduled(cron = "${orchestrator.schedule}")
	public void computeSocialMediaData() {
		log.info("running: {}", new Date());
		try {
			Future<List<Twit>> twits = service.getAllTwits();
			List<Twit> list = twits.get();
			log.info("twit size: {}", list.size());
			long start = System.currentTimeMillis();
			List<String> ids = list.stream().map(Twit::getId).collect(Collectors.toList());
			service.clearTwits(ids);
			for (Twit twit : list) {
				Future<SocialMedia> futureResponse = service.getTwitSentiment(twit);
				log.info("adding future: {}", twit.getId());
				socialMediaQueue.add(futureResponse);
			}
			long end = System.currentTimeMillis() - start;
			log.info("took to compute: {}", end);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	// TODO add a concurrent queue to keep track of twit time for deletion
	@Scheduled(cron = "*/50 * * * * *")
	public void computFutures() {
		int size = socialMediaQueue.size();
		log.info("running orchestrator backlog size: {} on: {}", size, new Date());
		for (int i = 0; i < size; i++) {
			Future<SocialMedia> futurePost = socialMediaQueue.poll();
			try {
				SocialMedia post = futurePost.get();
				String key = post.getKey();
				if (post.isUndefined()) {
					// TODO need a better way of handling undefined posts
					continue;
				}
				// TODO make computeData to keep track of text for highest scoring twits and keep them longer
				Map<String, Stats> result = post.computeData();
				Stats stat = result.get(key);
				BiFunction<String, Stats, Stats> biFunction = (k, v) -> v.aggregate(result.get(post.getKey()));
				if (stat.getNegatives() > 0 || stat.getPositives() > 0) {
					log.info("!!!!!!found a sentiment: {}", post);
				}
				statsMap.computeIfPresent(post.getKey(), biFunction);
				statsMap.putIfAbsent(post.getKey(), result.get(post.getKey()));
				postQueue.add(post.getPostBody());
				log.info("PRIORITY QUEUE: {}", postQueue.size());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0 */1 * * * *")
	private void deleteAllPosts() {
		log.info("stats map before: {}", statsMap);
		BiPredicate<Date, Date> pred = (currentTime, postTime) -> postTime.getTime() - currentTime.getTime() > 20000;
		if (!postQueue.isEmpty()) {
			while (pred.test(postQueue.peek().getCreatedDate(), new Date())) {
				BiFunction<String, Stats, Stats> biFunction = (k, v) -> v.aggregate(postQueue.peek());
				statsMap.computeIfPresent(postQueue.peek().getKey(), biFunction);
				log.info("POP: {}", postQueue.poll());
				if (postQueue.isEmpty()) {
					break;
				}
			}
		}
		log.info("stats map after: {}", statsMap);
	}

	@KafkaListener(topics = AppConstants.KAFKA_STOCK_TOPIC, groupId = AppConstants.KAFKA_STOCK_GROUP, containerFactory = "messageKafkaListenerFactory")
	public void consumeJson(StockDto stock) {
		log.info("Consumed stock: {}", stock);
		StockDocument stockDocument = new StockDocument();
		stockDocument.setStock(stock);
		Map<String, Stats> map = new HashMap<>();
		stockDocument.setStatsMap(map);
		for (String key : statsMap.keySet()) {
			Stats stats = statsMap.get(key);
			if (stats.getStockRelated().equalsIgnoreCase(stock.getStockName())) {
				map.put(key, stats);
			}
		}
		log.info("Sending document to elastic search {}", stockDocument);
		kafkaTemplate.send(AppConstants.KAFKA_STOCK_DOCUMENT_TOPIC, AppConstants.KAFKA_STOCK_KEY, stockDocument);
	}
}

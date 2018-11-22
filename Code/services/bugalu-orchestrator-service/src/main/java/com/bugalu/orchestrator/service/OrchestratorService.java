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
import java.util.function.Function;
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
import com.bugalu.domain.stock.TopScores;
import com.bugalu.domain.twitter.Post;
import com.bugalu.domain.twitter.SocialMedia;
import com.bugalu.domain.twitter.Stats;
import com.bugalu.domain.twitter.Twit;
import com.bugalu.domain.utils.AppConstants;
import com.google.common.collect.MinMaxPriorityQueue;

@Component
public class OrchestratorService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int SCORE_MAX_SIZE = 15;
	private static final long POST_LIMIT = 5;

	private static int COUNTER = 0;
	private final static String DOC_ID = "index-";

	private final ConcurrentRestClient service;
	private final ConcurrentLinkedQueue<Future<SocialMedia>> socialMediaQueue;
	private final PriorityQueue<Post> postQueue;
	// TODO change to map with key as the stock name
	private final MinMaxPriorityQueue<TopScores> goodScores;
	private final MinMaxPriorityQueue<TopScores> badScores;
	private final MinMaxPriorityQueue<TopScores> neutralScores;
	private final Map<String, Stats> statsMap;
	private final KafkaTemplate<String, StockDocument> kafkaTemplate;

	@Autowired
	public OrchestratorService(ConcurrentRestClient service, KafkaTemplate<String, StockDocument> kafkaTemplate) {
		this.service = service;
		this.kafkaTemplate = kafkaTemplate;
		this.postQueue = new PriorityQueue<>(Comparator.comparing(Post::getCreatedDate));
		this.socialMediaQueue = new ConcurrentLinkedQueue<>();
		this.statsMap = new ConcurrentHashMap<>();
		this.goodScores = MinMaxPriorityQueue.orderedBy(Comparator.comparing(TopScores::getScore).reversed())
				.maximumSize(15).create();
		this.badScores = MinMaxPriorityQueue.orderedBy(Comparator.comparing(TopScores::getScore).reversed())
				.maximumSize(15).create();
		this.neutralScores = MinMaxPriorityQueue.orderedBy(Comparator.comparing(TopScores::getScore).reversed())
				.maximumSize(15).create();
	}

	@Scheduled(cron = "*/20 * * * * *")
	public void computeSocialMediaData() {
		log.info("running: {}", new Date());
		try {
			Future<List<Twit>> twits = service.getAllTwits();
			List<Twit> list = twits.get();
			log.info("Computing twit size: {}", list.size());
			// TODO change how list is populated for deletion, (only add IDs after they have
			// been push to queue)
			List<String> ids = list.stream().map(Twit::getId).collect(Collectors.toList());
			service.clearTwits(ids);
			int counter = 0;
			for (Twit twit : list) {
				counter++;
				//TODO change payload to string and build twit after cliend received the response
				Future<SocialMedia> futureResponse = service.getTwitSentiment(twit);
				socialMediaQueue.add(futureResponse);
				if (counter == 5) {
					break;
				}
			}
			log.info("added future twits: {}", socialMediaQueue.size());// list.stream().map(Twit::getId).collect(Collectors.toList()));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "*/50 */2 * * * *")
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
				// TODO make computeData to keep track of text for highest scoring twits and
				// keep them longer
				Map<String, Stats> result = post.computeData();
				Stats stat = result.get(key);
				BiFunction<String, Stats, Stats> biFunction = (k, v) -> v.aggregate(result.get(post.getKey()));
				if (stat.getNegatives() > 0 || stat.getPositives() > 0) {
					log.info("!!!!!!found a sentiment: {}", post);
				}
				// TODO need to remove extra space/indentation/lines of text
				statsMap.computeIfPresent(post.getKey(), biFunction);
				statsMap.putIfAbsent(post.getKey(), result.get(post.getKey()));
				postQueue.add(post.getPostBody());
				populateMap(post);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		log.info("PRIORITY QUEUE: {}", postQueue.size());
		log.info("-------------------------MAPS-------------------------");
		log.info("good ones: {} - {}", goodScores.size(), goodScores);
		log.info("bad ones: {} - {}", badScores.size(), badScores);
		log.info("neutral ones: {} - {}", neutralScores.size(), neutralScores);
		log.info("sample sequence: ", neutralScores.stream().map(TopScores::getScore).collect(Collectors.toList()));
		log.info("-------------------------MAPS-------------------------");
	}

	@Scheduled(cron = "0 */5 * * * *")
	private void deleteAllPosts() {
		log.info("stats map before: {}", statsMap);
		BiPredicate<Date, Date> pred = (currentTime, postTime) -> postTime.getTime() - currentTime.getTime() > 500000L;
		if (!postQueue.isEmpty()) {
			while (pred.test(postQueue.peek().getCreatedDate(), new Date())) {
				BiFunction<String, Stats, Stats> biFunction = (k, v) -> v.aggregate(postQueue.peek());
				statsMap.computeIfPresent(postQueue.peek().getKey(), biFunction);
				deleteFromScoreMap(badScores, postQueue.peek().getId());
				deleteFromScoreMap(goodScores, postQueue.peek().getId());
				deleteFromScoreMap(neutralScores, postQueue.peek().getId());
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
		stockDocument.setId(DOC_ID + (++COUNTER));
		stockDocument.setStock(stock);
		stockDocument.setCreatedDate(new Date());
		Map<String, Stats> map = new HashMap<>();
		stockDocument.setStatsMap(map);
		for (String key : statsMap.keySet()) {
			Stats stats = statsMap.get(key);
			if (stats.getStockRelated().equalsIgnoreCase(stock.getStockName())) {
				map.put(key, stats);
			}
		}
		Function<MinMaxPriorityQueue<TopScores>, List<TopScores>> function = a -> a.stream()
				.sorted(Comparator.comparing(TopScores::getScore).reversed()).limit(POST_LIMIT)
				.collect(Collectors.toList());
		stockDocument.setBadScores(function.apply(badScores));
		stockDocument.setGoodScores(function.apply(goodScores));
		stockDocument.setNeutralScores(function.apply(neutralScores));
		log.info("Sending document to elastic search {}", stockDocument);
		kafkaTemplate.send(AppConstants.KAFKA_STOCK_DOCUMENT_TOPIC, AppConstants.KAFKA_STOCK_KEY, stockDocument);
	}

	private void populateMap(SocialMedia post) {
		Map<String, Stats> map = post.computeData();
		Stats stat = map.get(post.getKey());
		TopScores score = new TopScores();
		score.setId(post.getId());
		score.setText(post.getPostText());
		score.setScore(post.getPostScore());
		MinMaxPriorityQueue<TopScores> tempQueue;
		if (stat.getPositives() > 0) {
			tempQueue = goodScores;
		} else if (stat.getNegatives() > 0) {
			tempQueue = badScores;
		} else {
			tempQueue = neutralScores;
		}
		recomputeScoreMap(tempQueue, score);
	}

	private void recomputeScoreMap(MinMaxPriorityQueue<TopScores> map, TopScores score) {
		if (map.size() < SCORE_MAX_SIZE) {
			map.add(score);
		} else {
			TopScores lowest = map.peekLast();
			log.info("comparing -- largest in queue: {}, smallest in queue: {}, current to be added: {}, computed: {}",
					map.peekFirst(), lowest, score, lowest.compareTo(score));
			if (score.compareTo(lowest) >= 0) {
				TopScores removed = map.pollLast();
				map.add(score);
				log.info("removed: {}, for: {}", removed.getScore(), score.getScore());
			}
		}
	}

	private void deleteFromScoreMap(MinMaxPriorityQueue<TopScores> map, String idToRemove) {
		for (TopScores topScores : map) {
			if (topScores.getId().equals(idToRemove)) {
				map.remove(topScores);
				return;
			}
		}
	}
}

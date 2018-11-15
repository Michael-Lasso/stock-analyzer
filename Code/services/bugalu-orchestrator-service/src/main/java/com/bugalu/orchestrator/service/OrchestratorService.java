package com.bugalu.orchestrator.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bugalu.orchestrator.domain.SocialMedia;
import com.bugalu.orchestrator.domain.Stats;
import com.bugalu.orchestrator.domain.Twit;

@Component
public class OrchestratorService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final ConcurrentRestClient service;
	ConcurrentLinkedQueue<Future<SocialMedia>> socialMediaQueue;
	Map<String, Stats> statsMap;

	@Autowired
	public OrchestratorService(ConcurrentRestClient service) {
		this.service = service;
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
			List<String> ids = list.stream().map(Twit::getTopic).collect(Collectors.toList());
			service.clearTwits(ids);
			Map<String, Future<Twit>> map = new HashMap<>();
			// TODO aggregate twits after they have been scored
			// TODO add aggregated twits to stock price
			// TODO kafka producer will publis to elastic search topic
			for (Twit twit : list) {
				Future<SocialMedia> futureResponse = service.getTwitSentiment(twit);
				log.info("adding future: {}", twit.getTopic());
				socialMediaQueue.add(futureResponse);
			}
			long end = System.currentTimeMillis() - start;
			log.info("took to compute: {}", end);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

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
				Map<String, Stats> result = post.computeData();
				Stats stat = result.get(key);
				BiFunction<String, Stats, Stats> biFunction = (k, v) -> v.aggregate(result.get(post.getKey()));
				if (stat.getNegatives() > 0 || stat.getPositives() > 0) {
					log.info("!!!!!!found a sentiment: {}", post);
				}
				statsMap.computeIfPresent(post.getKey(), biFunction);
				statsMap.putIfAbsent(post.getKey(), result.get(post.getKey()));
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO add kafka consumer to consume stock topic
	// TODO combine stock with social media aggregation
	// TODO publish message to kafka to be consumed by elastic search consumer
	public void stockOrchestrator() {

	}
}

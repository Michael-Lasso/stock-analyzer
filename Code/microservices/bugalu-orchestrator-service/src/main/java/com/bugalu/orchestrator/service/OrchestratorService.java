package com.bugalu.orchestrator.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bugalu.orchestrator.adapter.TwitterProxy;
import com.bugalu.orchestrator.domain.Twit;

@Component
public class OrchestratorService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final ConcurrentRestClient service;

	private TwitterProxy proxy;

	@Autowired
	public OrchestratorService(ConcurrentRestClient service, TwitterProxy proxy) {
		this.service = service;
		this.proxy = proxy;
	}

	@Scheduled(cron = "${orchestrator.schedule}")
	public void reportCurrentTime() {
		log.info("running: {}", new Date());
		try {
			Future<List<Twit>> twits = service.getAllTwits();
			List<Twit> list = twits.get();
			long start = System.currentTimeMillis();
			List<String> ids = list.stream().map(Twit::getTopic).collect(Collectors.toList());
			service.clearTwits(ids);
			Map<String, Future<Twit>> map = new HashMap<>();
			for (Twit twit : list) {
				Twit t = new Twit();
				t.setText("this is a good day given that is raining and still have 3 days left of work");
				log.info("making an nlp call with {}", t);
				Future<Twit> futureResponse = service.getTwitSentiment(twit);
				map.put(twit.getTopic(), futureResponse);
			}
			List<Twit> twitList = map.values().stream().map(a -> {
				try {
					return a.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();// throw new RuntimeException();
				}
				return new Twit();
			}).collect(Collectors.toList());
			long end = System.currentTimeMillis() - start;
			twitList.stream().filter(a -> !a.getValue().equals("NEUTRAL"))
					.peek(a -> log.info("found: {}", a.toString()));
			log.info("took to compute: {}", end);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
}

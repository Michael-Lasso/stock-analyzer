package com.bugalu.twitter.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bugalu.domain.twitter.FutureTwit;
import com.bugalu.domain.twitter.Twit;

public class TwitterFilterService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final ConcurrentHashMap<String, Twit> map;
	private final ConcurrentLinkedQueue<FutureTwit> twitQue;
	private static volatile boolean running = true;
	private Thread thread;

	public TwitterFilterService(ConcurrentHashMap<String, Twit> map, ConcurrentLinkedQueue<FutureTwit> futureMap) {
		this.map = map;
		this.twitQue = futureMap;
	}

	public void start() {
		Runnable runnable = () -> {
			run();
		};
		thread = new Thread(runnable);
		thread.start();
	}

	public void run() {

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("shutting down twitter filter service!");
			running = false;
		}));

		while (running) {
			// try {
			// TimeUnit.SECONDS.sleep(25);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			if (!twitQue.isEmpty() && twitQue.peek().getLanguageFlag().isDone()) {

				// int currentSize = twitQue.size();
				// for (int i = 0; i < currentSize; i++) {
				try {
					FutureTwit futureTwit = twitQue.poll();
					boolean flag = futureTwit.getLanguageFlag().get();
					Twit twit = futureTwit.getTwit();
					if (flag) {
						map.put(twit.getId(), twit);
					} else {
						log.info("removing twit: {}", twit.getText());
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			// }
		}
	}

}

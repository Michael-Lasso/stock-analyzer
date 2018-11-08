package com.bugalu.twitter.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bugalu.twitter.domain.FutureTwit;
import com.bugalu.twitter.domain.Twit;

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
		while (running) {
			int currentSize = twitQue.size();
			for (int i = 0; i < currentSize; i++) {
				try {
					FutureTwit futureTwit = twitQue.poll();
					boolean flag = futureTwit.getLanguageFlag().get();
					Twit twit = futureTwit.getTwit();
					if (flag) {
						map.put(twit.getTopic(), twit);
					} else {
						log.info("removing twit: {}", twit.getText());
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				running = false;
			}));
		}
	}

}

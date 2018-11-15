package com.bugalu.orchestrator.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.domain.twitter.Twit;
import com.bugalu.orchestrator.adapter.TwitterProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController("/sentiments")
public class OrchestratorDataController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TwitterProxy proxy;

	@GetMapping
	@HystrixCommand(fallbackMethod = "fallBackCompute")
	public ResponseEntity<List<Twit>> computeSentimentValue() {
		log.info("hitting twits");
		return proxy.getAllTwits();
	}

	// @Scheduled(cron = "${orchestrator.schedule}")
	public void compute() {
		log.info("hitting twits");
		log.info("hitting: {}", proxy.getAllTwits());
	}

	public ResponseEntity<Twit> fallBackCompute() {
		Twit twit = new Twit();
		return ResponseEntity.ok(twit);
	}

}

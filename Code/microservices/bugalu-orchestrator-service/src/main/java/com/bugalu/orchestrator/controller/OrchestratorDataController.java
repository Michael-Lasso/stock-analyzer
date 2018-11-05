package com.bugalu.orchestrator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.orchestrator.domain.Twit;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController("/sentiments")
public class OrchestratorDataController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping
	@HystrixCommand(fallbackMethod = "fallBackCompute")
	public ResponseEntity<Twit> computeSentimentValue(@RequestBody Twit twit) {
		return ResponseEntity.ok(twit);
	}

	public ResponseEntity<Twit> fallBackCompute() {
		Twit twit = new Twit();
		return ResponseEntity.ok(twit);
	}

}

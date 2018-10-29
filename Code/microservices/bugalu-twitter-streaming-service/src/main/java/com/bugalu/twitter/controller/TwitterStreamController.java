package com.bugalu.twitter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.twitter.adapter.NLPAnalyzerProxy;
import com.bugalu.twitter.domain.Twit;
import com.bugalu.twitter.service.TwitterService;

@RestController
public class TwitterStreamController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NLPAnalyzerProxy proxy;

	@Autowired
	private TwitterService service;


	@GetMapping("/twits")
	public ResponseEntity<List<Twit>> getTwitList() {
		logger.info("getting list of twits");
		return ResponseEntity.ok(service.poll());
	}

	@GetMapping("/twits/{twit_id}")
	public ResponseEntity<Twit> getTwit(@PathVariable String twit_id) {
		Twit twit = service.getTwit(twit_id);
		ResponseEntity<Twit> response = proxy.computSentimentValue(twit);
		return response;
	}

}

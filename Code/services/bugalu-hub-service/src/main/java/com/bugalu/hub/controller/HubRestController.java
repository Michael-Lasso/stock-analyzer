package com.bugalu.hub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.domain.twitter.Sentiment;
import com.bugalu.domain.twitter.Twit;
import com.bugalu.hub.service.IndexService;

@RestController
public class HubRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IndexService nlp;

	@GetMapping("/index/{text}")
	public ResponseEntity<String> computeSentimentValue(@PathVariable String text) {
		logger.info("Received twit: {}", text);
		nlp.retrievePhrases(text);
		return ResponseEntity.ok(text);
	}

}

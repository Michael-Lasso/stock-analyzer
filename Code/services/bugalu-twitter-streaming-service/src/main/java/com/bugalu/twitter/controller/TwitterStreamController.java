package com.bugalu.twitter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.domain.twitter.Twit;
import com.bugalu.twitter.service.TwitterService;

@RestController
public class TwitterStreamController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TwitterService service;

	@GetMapping("/twits")
	public ResponseEntity<List<Twit>> getTwitList() {
		List<Twit> response = service.poll();
		log.info("getting list of twits: {}", response.size());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/twits/reset")
	public ResponseEntity<Boolean> clearTwitsById(@RequestBody List<String> idList) {
		log.info("reducing list by: {}", idList.size());
		boolean deleted = service.clearTwitsById(idList);
		return ResponseEntity.ok(deleted);
	}

}

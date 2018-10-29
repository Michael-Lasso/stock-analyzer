package com.bugalu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.adapter.CurrencyExchangeServiceProxy;
import com.bugalu.domain.Twit;
import com.bugalu.service.TwitterService;

@RestController
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CurrencyExchangeServiceProxy proxy;

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

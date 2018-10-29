package com.in28minutes.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/sentiment")
public class CurrencyExchangeController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NLPService nlp;

	@PostMapping
	public ResponseEntity<Twit> computeSentimentValue(@RequestBody Twit twit) {
		logger.info("Received twit: {}", twit);
		twit.setValue(nlp.computeSentiment(twit.getText()));
		return ResponseEntity.ok(twit);
	}
}

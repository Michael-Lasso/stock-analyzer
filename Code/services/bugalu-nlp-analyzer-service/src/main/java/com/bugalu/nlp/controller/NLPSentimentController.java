package com.bugalu.nlp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.domain.twitter.Language;
import com.bugalu.domain.twitter.Sentiment;
import com.bugalu.domain.twitter.Twit;
import com.bugalu.nlp.service.NLPService;

@RestController
public class NLPSentimentController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NLPService nlp;

	@PostMapping("/sentiments")
	public ResponseEntity<Twit> computeSentimentValue(@RequestBody Twit twit) {
		Sentiment response = Sentiment.valueOf(nlp.computeSentiment(twit.getText()));
		twit.setValue(response);
		logger.info("Received twit: {}", twit);
		return ResponseEntity.ok(twit);
	}

	@PostMapping("/sentiments/text")
	public ResponseEntity<Sentiment> computeSentimentValue(@RequestBody String text) {
		logger.info("Received text: {}", text);
		Sentiment response = Sentiment.valueOf(nlp.computeSentiment(text));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/languages")
	public ResponseEntity<Boolean> computeLanguage(@RequestBody Language language) {
		boolean response = nlp.filterLanguageByText(language.getText(), language.getLanguage());
		return ResponseEntity.ok(response);
	}

}

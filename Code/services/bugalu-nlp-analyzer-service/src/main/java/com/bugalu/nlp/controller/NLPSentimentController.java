package com.bugalu.nlp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.nlp.domain.Language;
import com.bugalu.nlp.domain.Twit;
import com.bugalu.nlp.service.NLPService;

@RestController
public class NLPSentimentController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NLPService nlp;

	@PostMapping("/sentiments")
	public ResponseEntity<Twit> computeSentimentValue(@RequestBody Twit twit) {
		logger.info("Received twit: {}", twit);
		twit.setValue(nlp.computeSentiment(twit.getText()));
		return ResponseEntity.ok(twit);
	}

	@PostMapping("/languages")
	public ResponseEntity<Boolean> computeLanguage(@RequestBody Language language) {
		logger.info("Detecting language");
		boolean response = nlp.filterLanguageByText(language.getText(), language.getLanguage());
		return ResponseEntity.ok(response);
	}

}

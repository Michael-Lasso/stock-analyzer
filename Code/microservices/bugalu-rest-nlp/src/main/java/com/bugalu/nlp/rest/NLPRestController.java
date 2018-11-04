package com.bugalu.nlp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bugalu.nlp.domain.Greeting;
import com.bugalu.nlp.domain.JsonData;
import com.bugalu.nlp.domain.ResultSet;
import com.bugalu.nlp.services.RecommendationService;

@RestController
@ConfigurationProperties(prefix = "access")
public class NLPRestController {

	private static final Logger log = LoggerFactory.getLogger(NLPRestController.class);

	@Autowired
	private RecommendationService recommendationService;

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "test") String name) {
		return new Greeting(0, name);
	}

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	ResponseEntity<?> add(@RequestBody JsonData input) {
		log.info("processing post request");
		try {
			ResultSet resultSet = new ResultSet();
			resultSet.setRecommendations(recommendationService.processRequest(input.getBusinesses()));
			return new ResponseEntity<ResultSet>(resultSet, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}

}

package com.bugalu.twitter.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bugalu.twitter.adapter.NLPAnalyzerProxy;
import com.bugalu.twitter.domain.Language;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Component
public class ConcurrentRestClient {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String hystrixStr = "asyncCall";

	private NLPAnalyzerProxy nlpProxy;

	@Autowired
	public ConcurrentRestClient(NLPAnalyzerProxy nlpProxy) {
		this.nlpProxy = nlpProxy;
	}

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "fallBackSentiment")
	public Future<Boolean> getTwitSentiment(Language language) {
		return new AsyncResult<Boolean>() {
			@Override
			public Boolean invoke() {
				ResponseEntity<Boolean> response = nlpProxy.detectLanguage(language);
				return response.getBody();
			}
		};
	}

	@HystrixCommand
	private Future<Boolean> fallBackSentiment(Language language) {
		return new AsyncResult<Boolean>() {
			@Override
			public Boolean invoke() {
				log.info("failed cal to nlp service");
				return Boolean.FALSE;
			}
		};
	}

}

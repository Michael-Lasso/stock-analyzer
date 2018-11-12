package com.bugalu.twitter.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bugalu.twitter.adapter.NLPAdapter;
import com.bugalu.twitter.domain.Language;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Component
public class ConcurrentRestClient {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String hystrixStr = "asyncCall";

	private NLPAdapter nlpProxy;

	@Autowired
	public ConcurrentRestClient(NLPAdapter nlpProxy) {
		this.nlpProxy = nlpProxy;
	}

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "fallBackLanguage")
	public Future<Boolean> getTextLanguage(Language language) {
		return new AsyncResult<Boolean>() {
			@Override
			public Boolean invoke() {
				ResponseEntity<String> response = nlpProxy.detectLanguage(language);
				log.info("response: {}", response);
				return response.getBody().contains("true");
			}
		};
	}

	@HystrixCommand
	private Future<Boolean> fallBackLanguage(Language language, Throwable ex) {
		return new AsyncResult<Boolean>() {
			@Override
			public Boolean invoke() {
				log.info("failed call to nlp service: {}", ex.getMessage());
				return Boolean.FALSE;
			}
		};
	}

}

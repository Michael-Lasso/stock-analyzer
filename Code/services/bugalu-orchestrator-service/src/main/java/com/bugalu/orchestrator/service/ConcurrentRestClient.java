package com.bugalu.orchestrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bugalu.domain.stock.StockDto;
import com.bugalu.domain.twitter.Sentiment;
import com.bugalu.domain.twitter.SocialMedia;
import com.bugalu.domain.twitter.Twit;
import com.bugalu.orchestrator.adapter.NLPAnalyzerProxy;
import com.bugalu.orchestrator.adapter.StockProxy;
import com.bugalu.orchestrator.adapter.TwitterProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Component
public class ConcurrentRestClient {
	private static final String hystrixStr = "asyncCall";
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private NLPAnalyzerProxy nlpProxy;
	private TwitterProxy twitterProxy;
	private StockProxy stockProxy;

	@Autowired
	public ConcurrentRestClient(NLPAnalyzerProxy nlpProxy, StockProxy stockProxy, TwitterProxy twitterProxy) {
		this.nlpProxy = nlpProxy;
		this.stockProxy = stockProxy;
		this.twitterProxy = twitterProxy;
	}

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "fallBackCompute")
	public Future<List<StockDto>> compute(String stockName) {
		return new AsyncResult<List<StockDto>>() {
			@Override
			public List<StockDto> invoke() {
				ResponseEntity<List<StockDto>> response = stockProxy.fetchStocks(stockName);
				return response.getBody();
			}
		};
	}

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "fallBackTwits")
	public Future<List<Twit>> getAllTwits() {
		return new AsyncResult<List<Twit>>() {
			@Override
			public List<Twit> invoke() {
				ResponseEntity<List<Twit>> response = twitterProxy.getAllTwits();
				return response.getBody();
			}
		};
	}

	@HystrixCommand(groupKey = "StoreSubmission", commandKey = "StoreSubmission", threadPoolKey = "StoreSubmission", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "180000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "180000"),
			@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000") }, threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "30"),
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000") }, fallbackMethod = "fallBackSentiment")
	public Future<SocialMedia> getTwitSentiment(Twit twit) {
		return new AsyncResult<SocialMedia>() {
			@Override
			public Twit invoke() {
				ResponseEntity<Twit> response = nlpProxy.computSentimentValue(twit);
				return response.getBody();
			}
		};
	}

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "fallBackResetTwitis")
	public Future<Boolean> clearTwits(List<String> idList) {
		return new AsyncResult<Boolean>() {
			@Override
			public Boolean invoke() {
				ResponseEntity<Boolean> response = twitterProxy.clearTwitsById(idList);
				return response.getBody();
			}
		};
	}

	@HystrixCommand
	private Future<SocialMedia> fallBackSentiment(Twit twit) {
		return new AsyncResult<SocialMedia>() {
			@Override
			public Twit invoke() {
				log.info("Undefined for: {}", twit);
				twit.setValue(Sentiment.UNDEFINED);
				return twit;
			}
		};
	}

	@HystrixCommand
	private Future<List<Twit>> fallBackTwits() {
		return new AsyncResult<List<Twit>>() {
			@Override
			public List<Twit> invoke() {
				return new ArrayList<>();
			}
		};
	}

	@HystrixCommand
	private List<StockDto> fallBackCompute(String stockName) {
		return new ArrayList<>();
	}

	@HystrixCommand
	private Boolean fallBackResetTwitis(List<String> idList) {
		return Boolean.FALSE;
	}
}

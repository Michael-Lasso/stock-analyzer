package com.bugalu.orchestrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bugalu.orchestrator.adapter.NLPAnalyzerProxy;
import com.bugalu.orchestrator.adapter.StockProxy;
import com.bugalu.orchestrator.adapter.TwitterProxy;
import com.bugalu.orchestrator.domain.StockDto;
import com.bugalu.orchestrator.domain.Twit;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Component
public class RestParallelClientService {
	private static final String hystrixStr = "asyncCall";

	@Autowired
	private NLPAnalyzerProxy nlpProxy;
	@Autowired
	private TwitterProxy twitterProxy;
	@Autowired
	private StockProxy stockProxy;

	// public RestParallelClientService(NLPAnalyzerProxy nlpProxy, StockProxy stockProxy,
	// TwitterProxy twitterProxy) {
	// this.nlpProxy = nlpProxy;
	// this.stockProxy = stockProxy;
	// this.twitterProxy = twitterProxy;
	// }

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
	public List<Twit> fallBackTwits() {
		return new ArrayList<>();
	}

	@HystrixCommand
	public List<StockDto> fallBackCompute(String stockName) {
		return new ArrayList<>();
	}

	@HystrixCommand
	public Boolean fallBackResetTwitis(List<String> idList) {
		return Boolean.FALSE;
	}
}

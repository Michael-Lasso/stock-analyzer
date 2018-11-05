package com.bugalu.orchestrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bugalu.orchestrator.adapter.NLPAnalyzerProxy;
import com.bugalu.orchestrator.adapter.TwitterProxy;
import com.bugalu.orchestrator.domain.Twit;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Component
public class OrchestratorService {
	private static final String hystrixStr = "asyncCall";

	@Autowired
	private NLPAnalyzerProxy nlpProxy;
	// @Autowired
	@Autowired
	private TwitterProxy twitterProxy;

	// public OrchestratorService(NLPAnalyzerProxy nlpProxy, StockProxy stockProxy,
	// TwitterProxy twitterProxy) {
	// this.nlpProxy = nlpProxy;
	// this.stockProxy = stockProxy;
	// this.twitterProxy = twitterProxy;
	// }

	// @HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr,
	// fallbackMethod = "")
	// public Future<List<StockDto>> compute(String stockName) {
	// return new AsyncResult<List<StockDto>>() {
	// @Override
	// public List<StockDto> invoke() {
	// ResponseEntity<List<StockDto>> response = stockProxy.fetchStocks(stockName);
	// return response.getBody();
	// }
	// };
	// }

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "")
	public Future<List<Twit>> getAllTwits() {
		return new AsyncResult<List<Twit>>() {
			@Override
			public List<Twit> invoke() {
				ResponseEntity<List<Twit>> response = twitterProxy.getAllTwits();
				return response.getBody();
			}
		};
	}

	public Future<List<Twit>> fallBackTwits() {
		return new AsyncResult<List<Twit>>() {
			@Override
			public List<Twit> invoke() {
				return new ArrayList<>();
			}
		};
	}

	// public Future<List<StockDto>> fallBackCompute(String stockName) {
	// return new AsyncResult<List<StockDto>>() {
	// @Override
	// public List<StockDto> invoke() {
	// return new ArrayList<>();
	// }
	// };
	// }
}

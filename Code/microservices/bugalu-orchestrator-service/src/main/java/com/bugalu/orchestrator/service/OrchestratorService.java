package com.bugalu.orchestrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bugalu.orchestrator.adapter.NLPAnalyzerProxy;
import com.bugalu.orchestrator.adapter.StockProxy;
import com.bugalu.orchestrator.adapter.TwitterProxy;
import com.bugalu.orchestrator.domain.StockDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Service
public class OrchestratorService {
	private static final String hystrixStr = "asyncCall";

	private final NLPAnalyzerProxy nlpProxy;
	private final StockProxy stockProxy;
	private final TwitterProxy twitterProxy;

	@Autowired
	public OrchestratorService(NLPAnalyzerProxy nlpProxy, StockProxy stockProxy, TwitterProxy twitterProxy) {
		this.nlpProxy = nlpProxy;
		this.stockProxy = stockProxy;
		this.twitterProxy = twitterProxy;
	}

	@HystrixCommand(groupKey = hystrixStr, commandKey = hystrixStr, fallbackMethod = "")
	public Future<List<StockDto>> compute(String stockName) {
		return new AsyncResult<List<StockDto>>() {
			@Override
			public List<StockDto> invoke() {
				ResponseEntity<List<StockDto>> response = stockProxy.fetchStocks(stockName);
				return response.getBody();
			}
		};
	}

	public Future<List<StockDto>> fallBackCompute(String stockName) {
		return new AsyncResult<List<StockDto>>() {
			@Override
			public List<StockDto> invoke() {
				return new ArrayList<>();
			}
		};
	}
}

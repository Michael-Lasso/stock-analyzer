package com.bugalu.orchestrator.adapter;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bugalu.domain.stock.StockDto;

@RibbonClient(name = "bugalu-stock-streaming-service")
@FeignClient(name = "bugalu-api-gateway-server")
public interface StockProxy {

	@PostMapping("bugalu-stock-streaming-service/stocks/{stockName}")
	public ResponseEntity<List<StockDto>> fetchStocks(@PathVariable("stockName") String stockName);

	@PostMapping("bugalu-stock-streaming-service/stocks/")
	public ResponseEntity<Map<String, List<StockDto>>> fetchAllStocks();

	@PostMapping("bugalu-stock-streaming-service/stocks/resets")
	public ResponseEntity<String> resetStocks();
}

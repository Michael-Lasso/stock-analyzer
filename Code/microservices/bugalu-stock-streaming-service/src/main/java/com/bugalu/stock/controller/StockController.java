package com.bugalu.stock.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bugalu.stock.domain.StockDto;
import com.bugalu.stock.stock.StockBatchService;

@Controller
public class StockController {

	@Autowired
	private StockBatchService service;

	@GetMapping("/stocks/{stockName}")
	public ResponseEntity<List<StockDto>> getStockDetails(@PathVariable String stockName) throws Exception {
		return ResponseEntity.ok(service.getStocks(stockName));
	}

	@GetMapping("/stocks")
	public ResponseEntity<Map<String, List<StockDto>>> getAllStock() throws Exception {
		return ResponseEntity.ok(service.getAllStocks());
	}

	@PostMapping("/stocks/reset")
	public ResponseEntity<String> clearAllStock() {
		return ResponseEntity.ok("Stock list clear");
	}

}

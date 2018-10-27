package com.basant.spring.yahoo.stock.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.basant.spring.yahoo.stock.api.dto.Headers;
import com.basant.spring.yahoo.stock.api.dto.StockDto;
import com.basant.spring.yahoo.stock.api.dto.StockHistory;
import com.basant.spring.yahoo.stock.api.service.StockService;

@Controller
public class StockController {

	@Autowired
	private StockService service;

	@GetMapping("/getStock")
	public String getStockDetails(@RequestParam("stockName") String stockName, Model model) throws Exception {
		StockDto stockDto = service.getStockInfo(stockName);
		List<StockDto> stockList = new ArrayList<>();
		stockList.add(stockDto);
		model.addAttribute("stockList", stockList);
		return "stock";
	}

	@GetMapping("/getHistory/{stockName}")
	public String getHistory(@PathVariable String stockName, Model model) throws Exception {
		List<Headers> headers = getHeaders();
		List<StockHistory> histories = service.getHistory(stockName, 0, null);
		model.addAttribute("msg", "Stock History up to today :");
		model.addAttribute("headers", headers);
		model.addAttribute("histories", histories);
		return "stock";
	}

	@GetMapping("/exportStockHistory")
	public String exportHistory(@RequestParam("stockName") String stockName, @RequestParam("year") int year,
			@RequestParam("field") String searchType, Model model) throws Exception {
		List<Headers> headers = getHeaders();
		List<StockHistory> histories = service.getHistory(stockName, year, searchType);
		model.addAttribute("msg",
				year + " Year " + searchType.toLowerCase() + " Stock History of " + stockName + " : ");
		model.addAttribute("headers", headers);
		model.addAttribute("histories", histories);
		return "stock";
	}

	private List<Headers> getHeaders() {
		List<Headers> headers = new ArrayList<>();
		headers.add(new Headers("Symbol", "Date", "High(price)", "Low(price)", "closed(price)"));
		return headers;
	}
}

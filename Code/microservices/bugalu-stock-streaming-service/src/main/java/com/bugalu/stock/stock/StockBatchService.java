package com.bugalu.stock.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bugalu.stock.domain.StockDto;

@Service
public class StockBatchService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private Thread thread;
	private Map<String, List<StockDto>> map;
	private final StockService stockService;
	private static volatile boolean running = true;

	@Value("#{'${stock.service.stocks}'.split(',')}")
	private List<String> myList;

	@Autowired
	public StockBatchService(StockService stockService) {
		this.stockService = stockService;
	}

	@PostConstruct
	public void init() {
		map = new ConcurrentHashMap<>();
		Runnable runnable = () -> {
			run();
		};
		thread = new Thread(runnable);
		thread.start();
	}

	private void run() {
		while (running) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				running = false;
			}));

			try {
				List<StockDto> stocks = stockService.getStocksInfo(myList);
				stocks.forEach(stock -> {
					map.putIfAbsent(stock.getStockName(), new ArrayList<>());
					map.get(stock.getStockName()).add(stock);
				});
				log.info("new stocks added: {}", stocks);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				TimeUnit.MINUTES.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public List<StockDto> getStocks(String key) {
		return map.get(key);
	}

	public Map<String, List<StockDto>> getAllStocks() {
		return map;
	}

	public void clearStocks() {
		map.clear();
	}
}

package com.bugalu.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bugalu.domain.stock.StockDto;
import com.bugalu.domain.utils.AppConstants;

@Component
public class StockBatchService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final KafkaTemplate<String, StockDto> kafkaTemplate;
	private final StockService stockService;
	private Map<String, List<StockDto>> map;

	@Value("#{'${stock.service.stocks:TSLA,SPY}'.split(',')}")
	private List<String> myList;

	@Autowired
	public StockBatchService(StockService stockService, KafkaTemplate<String, StockDto> kafkaTemplate) {
		this.stockService = stockService;
		this.kafkaTemplate = kafkaTemplate;
		map = new ConcurrentHashMap<>();
	}

	@Scheduled(cron = "* */2 * * * *")
	private void run() {
		try {
			log.info("running for list: {}", myList);
			List<StockDto> stocks = stockService.getStocksInfo(myList);
			stocks.forEach(stock -> {
				map.putIfAbsent(stock.getStockName(), new ArrayList<>());
				map.get(stock.getStockName()).add(stock);
				kafkaTemplate.send(AppConstants.STOCK_TOPIC, AppConstants.KAFKA_STOCK_KEY, stock);
				log.info("new stocks published: {}", stocks);
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	@KafkaListener(topics = "stock_topic", groupId = "group_json", containerFactory = "messageKafkaListenerFactory")
	public void consumeJson(StockDto stock) {
		System.out.println("Consumed JSON Message: " + stock);
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

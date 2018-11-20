package com.bugalu.hub.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bugalu.domain.stock.StockDocument;
import com.bugalu.domain.utils.AppConstants;
import com.bugalu.hub.domain.Customers;

@Component
public class IndexServiceImpl implements IndexService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DocumentStockRepository repository;

	@Override
	public List<String> retrievePhrases(String text) {
		Customers c = new Customers();
		c.setFirstName("test".concat(UUID.randomUUID().toString()));
		c.setId(text);
		c.setLastName("lasso");
		repository.save(c);
		return null;
	}

	@Override
	public String computeSentiment(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> detectLanguate(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean filterLanguageByText(String text, String language) {
		// TODO Auto-generated method stub
		return false;
	}

	@KafkaListener(topics = AppConstants.KAFKA_STOCK_DOCUMENT_TOPIC, groupId = AppConstants.KAFKA_STOCK_DOCUMENT_GROUP, containerFactory = "messageKafkaListenerFactory")
	public void consumeJson(StockDocument stock) {
		log.info("consumin stock document: {}", stock);
	}

}

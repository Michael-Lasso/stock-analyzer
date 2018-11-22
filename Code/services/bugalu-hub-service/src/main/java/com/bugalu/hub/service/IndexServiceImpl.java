package com.bugalu.hub.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bugalu.domain.stock.StockDocument;
import com.bugalu.domain.utils.AppConstants;
import com.bugalu.hub.domain.Customers;
import com.bugalu.hub.domain.IndexStockDocument;
import com.bugalu.hub.domain.StockDocumentTranslator;

@Component
public class IndexServiceImpl implements IndexService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DocumentStockRepository repository;

	@Override
	public void testIndex(String text) {
		Customers c = new Customers();
		c.setFirstName("test".concat(UUID.randomUUID().toString()));
		c.setId(text);
		c.setLastName("lasso");
		// repository.save(c);
	}

	@KafkaListener(topics = AppConstants.KAFKA_STOCK_DOCUMENT_TOPIC, groupId = AppConstants.KAFKA_STOCK_DOCUMENT_GROUP, containerFactory = "messageKafkaListenerFactory")
	public void consumeJson(StockDocument stock) {
		IndexStockDocument indexDocument = StockDocumentTranslator.translate(stock);
		log.info("Document ready for indexing: {}", indexDocument);
		// index document
		repository.save(indexDocument);
	}

}

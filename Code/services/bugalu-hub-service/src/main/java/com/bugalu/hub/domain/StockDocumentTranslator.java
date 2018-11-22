package com.bugalu.hub.domain;

import com.bugalu.domain.stock.StockDocument;

public class StockDocumentTranslator {

	public static IndexStockDocument translate(StockDocument stockDocument) {
		IndexStockDocument indexDocument = new IndexStockDocument();
		indexDocument.setBadScores(stockDocument.getBadScores());
		indexDocument.setGoodScores(stockDocument.getGoodScores());
		indexDocument.setNeutralScores(stockDocument.getNeutralScores());
		indexDocument.setStatsMap(stockDocument.getStatsMap());
		indexDocument.setStock(stockDocument.getStock());
		indexDocument.setId(stockDocument.getId());
		indexDocument.setCreatedDate(stockDocument.getCreatedDate());
		return indexDocument;
	}

}

package com.bugalu.domain.stock;

import java.util.Map;

import com.bugalu.domain.twitter.Stats;

public class StockDocument {
	private StockDto stock;
	Map<String, Stats> statsMap;

	public StockDto getStock() {
		return stock;
	}

	public void setStock(StockDto stock) {
		this.stock = stock;
	}

	public Map<String, Stats> getStatsMap() {
		return statsMap;
	}

	public void setStatsMap(Map<String, Stats> statsMap) {
		this.statsMap = statsMap;
	}

	@Override
	public String toString() {
		return "StockDocument [stock=" + stock + ", statsMap=" + statsMap + "]";
	}

}

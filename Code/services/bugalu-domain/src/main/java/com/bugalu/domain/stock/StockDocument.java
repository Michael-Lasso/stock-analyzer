package com.bugalu.domain.stock;

import java.util.List;
import java.util.Map;

import com.bugalu.domain.twitter.Stats;

public class StockDocument {

	private String id;
	private StockDto stock;
	private Map<String, Stats> statsMap;
	private List<TopScores> goodScores;
	private List<TopScores> badScores;
	private List<TopScores> neutralScores;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TopScores> getGoodScores() {
		return goodScores;
	}

	public void setGoodScores(List<TopScores> goodScores) {
		this.goodScores = goodScores;
	}

	public List<TopScores> getBadScores() {
		return badScores;
	}

	public void setBadScores(List<TopScores> badScores) {
		this.badScores = badScores;
	}

	public List<TopScores> getNeutralScores() {
		return neutralScores;
	}

	public void setNeutralScores(List<TopScores> neutralScores) {
		this.neutralScores = neutralScores;
	}

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

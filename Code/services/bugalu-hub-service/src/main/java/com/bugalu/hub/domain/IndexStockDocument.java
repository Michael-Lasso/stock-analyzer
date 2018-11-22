package com.bugalu.hub.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.bugalu.domain.stock.StockDto;
import com.bugalu.domain.stock.TopScores;
import com.bugalu.domain.twitter.Stats;

@Document(indexName = "stock_document", type = "document", shards = 1, replicas = 0, refreshInterval = "-1")
public class IndexStockDocument {

	@Id
	private String id;
	private StockDto stock;
	private Map<String, Stats> statsMap;
	private List<TopScores> goodScores;
	private List<TopScores> badScores;
	private List<TopScores> neutralScores;
	private Date createdDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

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
		return "IndexStockDocument [id=" + id + ", stock=" + stock + ", statsMap=" + statsMap + ", goodScores="
				+ goodScores + ", badScores=" + badScores + ", neutralScores=" + neutralScores + ", createdDate="
				+ createdDate + "]";
	}

}

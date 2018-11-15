package com.bugalu.domain.stock;

import java.math.BigDecimal;
import java.util.Date;

public class StockDto {
	private String name;
	private BigDecimal price;
	private BigDecimal change;
	private String currency;
	private BigDecimal bid;
	private BigDecimal priceHint;
	private String stockName;
	private Date timeCreated;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public BigDecimal getPriceHint() {
		return priceHint;
	}

	public void setPriceHint(BigDecimal priceHint) {
		this.priceHint = priceHint;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public StockDto() {
	}

	public StockDto(String name, BigDecimal price, BigDecimal change, String currency, BigDecimal bid,
			BigDecimal priceHint, String stockName, Date timeCreated) {
		super();
		this.name = name;
		this.price = price;
		this.change = change;
		this.currency = currency;
		this.bid = bid;
		this.priceHint = priceHint;
		this.stockName = stockName;
		this.timeCreated = timeCreated;
	}

	@Override
	public String toString() {
		return "StockDto [name=" + name + ", price=" + price + ", change=" + change + ", currency=" + currency
				+ ", bid=" + bid + ", priceHint=" + priceHint + ", stockName=" + stockName + ", timeCreated="
				+ timeCreated + "]";
	}

}

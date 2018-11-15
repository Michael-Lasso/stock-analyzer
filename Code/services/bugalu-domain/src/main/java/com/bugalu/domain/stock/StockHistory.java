package com.bugalu.domain.stock;

import java.math.BigDecimal;

public class StockHistory {
	private String symbol;
	private String date;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public StockHistory(String symbol, String date, BigDecimal high, BigDecimal low, BigDecimal close) {
		super();
		this.symbol = symbol;
		this.date = date;
		this.high = high;
		this.low = low;
		this.close = close;
	}

	public StockHistory() {
		super();
	}

	@Override
	public String toString() {
		return "StockHistory [symbol=" + symbol + ", date=" + date + ", high=" + high + ", low=" + low + ", close="
				+ close + "]";
	}
	
	

}

package com.bugalu.stock.stock;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bugalu.stock.domain.StockDto;
import com.bugalu.stock.domain.StockHistory;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Service
public class StockService {

	public StockDto getStockInfo(String stockName) throws Exception {
		Stock stock = YahooFinance.get(stockName.toUpperCase(), true);
		return new StockDto(stock.getName(), stock.getQuote().getPrice(), stock.getQuote().getChange(),
				stock.getCurrency(), stock.getQuote().getBid(), stock.getQuote().getChangeFromYearHighInPercent(),
				stockName.toUpperCase(), new Date());
	}

	public List<StockDto> getStocksInfo(List<String> list) throws Exception {
		List<StockDto> stockList = new ArrayList<>();
		Map<String, Stock> stockMap = YahooFinance.get(list.toArray(new String[list.size()]));
		for (String key : stockMap.keySet()) {
			Stock stock = stockMap.get(key);
			StockDto stockObject = new StockDto(stock.getName(), stock.getQuote().getPrice(),
					stock.getQuote().getChange(), stock.getCurrency(), stock.getQuote().getBid(),
					stock.getQuote().getChangeFromYearHighInPercent(), key.toUpperCase(), new Date());
			stockList.add(stockObject);
		}
		return stockList;
	}

	public List<StockHistory> getHistory(String stockName, int year, String searchType) throws Exception {
		List<StockHistory> histories = new ArrayList<>();
		Stock stock = null;
		List<HistoricalQuote> quotes = null;
		if ((searchType == null || searchType.length() <= 1) && (year == 0)) {
			stock = YahooFinance.get(stockName, true);
		} else {
			Calendar from = Calendar.getInstance();
			Calendar to = Calendar.getInstance();
			from.add(Calendar.YEAR, Integer.valueOf("-" + year));
			stock = YahooFinance.get(stockName);
			quotes = stock.getHistory(from, to, getInterval(searchType));
		}

		quotes = stock.getHistory();
		for (HistoricalQuote quote : quotes) {
			histories.add(new StockHistory(quote.getSymbol(), convertor(quote.getDate()), quote.getHigh(),
					quote.getLow(), quote.getClose()));

		}
		return histories;
	}

	private String convertor(Calendar cal) {
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(cal.getTime());
		return formatted;
	}

	private Interval getInterval(String searchType) {
		Interval interval = null;
		switch (searchType) {
		case "DAILY":
			interval = Interval.DAILY;
			break;
		case "WEEKLY":
			interval = Interval.WEEKLY;
			break;
		case "MONTHLY":
			interval = Interval.MONTHLY;
			break;

		}
		return interval;
	}

	public Stock getStock(String stockName) throws IOException {
		return YahooFinance.get(stockName.toUpperCase(), true);
	}
}

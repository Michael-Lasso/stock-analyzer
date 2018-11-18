package com.bugalu.domain.twitter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bugalu.domain.utils.AppConstants;

public class Twit implements SocialMedia {

	private String id;
	private String text;
	private int countWeight;
	private Sentiment value;
	private Date dateCreated;
	private List<String> terms;
	private String stockRelated;

	public Twit() {
	}

	public Twit(String id, String text, int countWeight, Sentiment value) {
		super();
		this.id = id;
		this.text = text;
		this.countWeight = countWeight;
		this.value = value;
	}

	public String getStockRelated() {
		return stockRelated;
	}

	public void setStockRelated(String stockRelated) {
		this.stockRelated = stockRelated;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	public String getId() {
		return id;
	}

	public void setId(String topic) {
		this.id = topic;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCountWeight() {
		return countWeight;
	}

	public void setCountWeight(int countWeight) {
		this.countWeight = countWeight;
	}

	public Sentiment getValue() {
		return value;
	}

	public void setValue(Sentiment value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Twit [id=" + id + ", text=" + text + ", countWeight=" + countWeight + ", value=" + value + "]";
	}

	@Override
	public Map<String, Stats> computeData() {
		Map<String, Stats> result = new HashMap<>();
		Stats stats = new Stats();
		stats.setStockRelated(stockRelated);
		stats.setCount(1);
		if (value.equals(Sentiment.NEGATIVE)) {
			stats.setNegatives(1);
			stats.setNegativeWeight(countWeight);
		} else if (value.equals(Sentiment.POSITIVE)) {
			stats.setPositives(1);
			stats.setPositiveWeight(countWeight);
		} else {
			stats.setNeutralWeight(countWeight);
		}
		result.put(AppConstants.TWIT, stats);
		return result;
	}

	@Override
	public String getKey() {
		return AppConstants.TWIT;
	}

	@Override
	public boolean isUndefined() {
		return value == null || value.equals(Sentiment.UNDEFINED);
	}

	@Override
	public Post getPostBody() {
		return new Post(dateCreated, value, countWeight, AppConstants.TWIT);
	}

}

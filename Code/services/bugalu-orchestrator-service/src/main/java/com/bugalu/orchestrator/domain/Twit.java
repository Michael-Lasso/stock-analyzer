package com.bugalu.orchestrator.domain;

import java.util.HashMap;
import java.util.Map;

import com.bugalu.orchestrator.utils.AppConstants;

public class Twit implements SocialMedia {

	private String topic;
	private String text;
	private int countWeight;
	private Sentiment value;

	public Twit() {
	}

	public Twit(String topic, String text, int countWeight, Sentiment value) {
		super();
		this.topic = topic;
		this.text = text;
		this.countWeight = countWeight;
		this.value = value;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
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
		return "Twit [topic=" + topic + ", text=" + text + ", countWeight=" + countWeight + ", value=" + value + "]";
	}

	@Override
	public Map<String, Stats> computeData() {
		Map<String, Stats> result = new HashMap<>();
		Stats stats = new Stats();
		stats.setCount(1);
		if (value.equals(Sentiment.NEGATIVE)) {
			stats.setNegatives(1);
		} else {
			stats.setPositives(1);
		}
		result.put(AppConstants.TWIT, stats);
		return result;
	}

	@Override
	public String getKey() {
		return AppConstants.TWIT;
	}

}

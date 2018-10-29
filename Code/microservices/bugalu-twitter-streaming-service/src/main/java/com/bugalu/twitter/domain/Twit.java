package com.bugalu.twitter.domain;

public class Twit {
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
}

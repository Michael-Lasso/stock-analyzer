package com.bugalu.nlp.domain;

public class Twit {
	private String topic;
	private String text;
	private int countWeight;
	private String value;

	public Twit() {
	}

	public Twit(String topic, String text, int countWeight, String value) {
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Twit [topic=" + topic + ", text=" + text + ", countWeight=" + countWeight + ", value=" + value + "]";
	}

}

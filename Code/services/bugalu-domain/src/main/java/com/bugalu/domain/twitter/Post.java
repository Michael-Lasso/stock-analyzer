package com.bugalu.domain.twitter;

import java.util.Date;

public class Post {
	private Date createdDate;
	private Sentiment sentiment;
	private int weightCount;
	private String key;

	public Post(Date createdDate, Sentiment sentiment, int weightCount, String key) {
		this.createdDate = createdDate;
		this.sentiment = sentiment;
		this.weightCount = weightCount;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Sentiment getSentiment() {
		return sentiment;
	}

	public int getWeightCount() {
		return weightCount;
	}

	@Override
	public String toString() {
		return "Post [createdDate=" + createdDate + ", sentiment=" + sentiment + ", weightCount=" + weightCount
				+ ", key=" + key + "]";
	}

}

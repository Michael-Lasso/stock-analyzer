package com.bugalu.domain.twitter;

import java.util.Date;

public class Post {
	private final Date createdDate;
	private final Sentiment sentiment;
	private final int weightCount;
	private final String key;
	private final String id;

	public Post(Date createdDate, Sentiment sentiment, int weightCount, String key, String id) {
		this.createdDate = createdDate;
		this.sentiment = sentiment;
		this.weightCount = weightCount;
		this.key = key;
		this.id = id;
	}

	public String getId() {
		return id;
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

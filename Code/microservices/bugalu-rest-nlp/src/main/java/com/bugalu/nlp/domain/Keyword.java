
package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.Map;

import com.bugalu.nlp.utils.Sentiment;

public class Keyword {

	private String name;
	private Integer rating;
	private String sentiment;
	private long likes;
	private long dislikes;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public void combine(Keyword keyword) {
		this.likes += keyword.likes;
		this.dislikes += keyword.dislikes;
	}

	public Keyword(String name, String sentiment) {
		this.name = name;
		this.sentiment = sentiment;
		if (sentiment.equals(Sentiment.POSITIVE.toString())) {
			this.likes = 1;
		} else if (sentiment.equals(Sentiment.NEGATIVE.toString())) {
			this.dislikes = 1;
		}
	}

	public Keyword() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public long getLikes() {
		return likes;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public long getDislikes() {
		return dislikes;
	}

	public void setDislikes(long dislikes) {
		this.dislikes = dislikes;
	}

	public boolean equals(Object o) {
		return (o instanceof Keyword) && ((Keyword) o).getName().equals(this.getName());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

}

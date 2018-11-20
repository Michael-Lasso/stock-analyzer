package com.bugalu.domain.stock;

import java.util.Comparator;

public class TopScores implements Comparable<TopScores> {
	private String id;
	private String text;
	private long score;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	@Override
	public int compareTo(TopScores o) {
		return Long.compare(this.score, o.score);
	}

	@Override
	public String toString() {
		return "TopScores [id=" + id + ", text=" + text + ", score=" + score + "]";
	}

}

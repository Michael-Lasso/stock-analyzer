package com.bugalu.domain.twitter;

public class Stats {

	private int count;
	private int positives;
	private int negatives;
	private long positiveWeight;
	private long negativeWeight;
	private long neutralWeight;
	private String stockRelated;

	public int getCount() {
		return count;
	}

	public String getStockRelated() {
		return stockRelated;
	}

	public void setStockRelated(String stockRelated) {
		this.stockRelated = stockRelated;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPositives() {
		return positives;
	}

	public void setPositives(int positives) {
		this.positives = positives;
	}

	public int getNegatives() {
		return negatives;
	}

	public void setNegatives(int negatives) {
		this.negatives = negatives;
	}

	public long getPositiveWeight() {
		return positiveWeight;
	}

	public void setPositiveWeight(long positiveWeight) {
		this.positiveWeight = positiveWeight;
	}

	public long getNegativeWeight() {
		return negativeWeight;
	}

	public void setNegativeWeight(long negativeWeight) {
		this.negativeWeight = negativeWeight;
	}

	public long getNeutralWeight() {
		return neutralWeight;
	}

	public void setNeutralWeight(long neutralWeight) {
		this.neutralWeight = neutralWeight;
	}

	public Stats aggregate(Stats stats) {
		this.count++;
		this.positives += stats.positives;
		this.negatives += stats.negatives;
		this.negativeWeight += stats.negativeWeight;
		this.positiveWeight += stats.positiveWeight;
		this.neutralWeight += stats.neutralWeight;
		return this;
	}

	public Stats aggregate(Post post) {
		this.count--;
		if (post.getSentiment().equals(Sentiment.NEGATIVE)) {
			this.negatives--;
			this.negativeWeight -= post.getWeightCount();
		} else if (post.getSentiment().equals(Sentiment.POSITIVE)) {
			this.positives--;
			this.positiveWeight -= post.getWeightCount();
		} else {
			this.neutralWeight -= post.getWeightCount();
		}
		return this;
	}

	@Override
	public String toString() {
		return "Stats [count=" + count + ", positives=" + positives + ", negatives=" + negatives + ", positiveWeight="
				+ positiveWeight + ", negativeWeight=" + negativeWeight + ", neutralWeight=" + neutralWeight
				+ ", stockRelated=" + stockRelated + "]";
	}

}

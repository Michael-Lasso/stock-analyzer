package com.bugalu.orchestrator.domain;

public class Stats {

	private int count;
	private int positives;
	private int negatives;

	public int getCount() {
		return count;
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

	public Stats aggregate(Stats stats) {
		this.count++;
		this.positives += stats.positives;
		this.negatives += stats.negatives;
		return this;
	}

}

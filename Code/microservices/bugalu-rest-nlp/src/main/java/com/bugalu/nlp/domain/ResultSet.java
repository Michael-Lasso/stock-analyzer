package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSet {
	private List<Recommendation> recommendations = null;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public List<Recommendation> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<Recommendation> recommendations) {
		this.recommendations = recommendations;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}

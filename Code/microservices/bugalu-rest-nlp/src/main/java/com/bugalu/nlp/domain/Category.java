package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.Map;

public class Category {

	private String alias;
	private String title;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

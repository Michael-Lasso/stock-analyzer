package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.Map;

public class Review {

	private Integer id;
	private String rev_text;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public String getRev_text() {
		return rev_text;
	}

	public void setRev_text(String rev_text) {
		this.rev_text = rev_text;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

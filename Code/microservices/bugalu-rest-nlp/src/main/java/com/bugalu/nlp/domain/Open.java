package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.Map;

public class Open {

	private Boolean isOvernight;
	private String start;
	private String end;
	private Integer day;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Boolean getIsOvernight() {
		return isOvernight;
	}

	public void setIsOvernight(Boolean isOvernight) {
		this.isOvernight = isOvernight;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

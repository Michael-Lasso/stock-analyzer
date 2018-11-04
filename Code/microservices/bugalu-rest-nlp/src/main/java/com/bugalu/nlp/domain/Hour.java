package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hour {

	private List<Open> open = null;
	private String hoursType;
	private Boolean isOpenNow;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public List<Open> getOpen() {
		return open;
	}

	public void setOpen(List<Open> open) {
		this.open = open;
	}

	public String getHoursType() {
		return hoursType;
	}

	public void setHoursType(String hoursType) {
		this.hoursType = hoursType;
	}

	public Boolean getIsOpenNow() {
		return isOpenNow;
	}

	public void setIsOpenNow(Boolean isOpenNow) {
		this.isOpenNow = isOpenNow;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}


package com.bugalu.nlp.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommendation {

	private String name;
	private String imageUrl;
	private String cusine;
	private String price;
	private Double distance;
	private String zone;
	private List<Hour> hours = null;
	private String link;
	private List<Keyword> keywords = null;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public static Recommendation factory(Business business) {
		Recommendation recommendation = new Recommendation();
		recommendation.setName(business.getName());
		recommendation.setPrice(business.getPrice());
		recommendation.setHours(business.getHours());
		recommendation.setImageUrl(business.getImageUrl());
		recommendation.setCusine(business.getCategories().toString().replaceAll("[|]", ""));
		recommendation.setLink(business.getUrl());
		recommendation.setDistance(business.getDistance());
		return recommendation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCusine() {
		return cusine;
	}

	public void setCusine(String cusine) {
		this.cusine = cusine;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public List<Hour> getHours() {
		return hours;
	}

	public void setHours(List<Hour> hours) {
		this.hours = hours;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}

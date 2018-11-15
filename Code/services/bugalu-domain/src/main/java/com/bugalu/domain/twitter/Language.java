package com.bugalu.domain.twitter;

public class Language {

	private String text;
	private String language;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Language(String text, String language) {
		super();
		this.text = text;
		this.language = language;
	}

	public Language() {
		super();
	}

}

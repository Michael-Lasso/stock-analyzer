package com.bugalu.nlp.domain;

import java.util.List;

public class Data {
	private String name;
	private List<String> rev_text;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRev_text() {
		return rev_text;
	}

	public void setRev_text(List<String> rev_text) {
		this.rev_text = rev_text;
	}

}

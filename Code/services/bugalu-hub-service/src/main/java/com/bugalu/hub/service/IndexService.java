package com.bugalu.hub.service;

import java.util.List;
import java.util.Map;

public interface IndexService {

	List<String> retrievePhrases(String text);

	String computeSentiment(String text);

	Map<String, Double> detectLanguate(String text);

	boolean filterLanguageByText(String text, String language);

}

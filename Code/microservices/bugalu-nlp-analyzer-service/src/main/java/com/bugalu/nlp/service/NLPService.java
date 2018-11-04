package com.bugalu.nlp.service;

import java.util.List;
import java.util.Map;

public interface NLPService {

	public List<String> retrievePhrases(String text);

	public String computeSentiment(String text);

	public Map<String, Double> detectLanguate(String text);

}

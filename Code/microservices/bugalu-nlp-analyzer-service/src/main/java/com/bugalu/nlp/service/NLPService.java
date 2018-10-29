package com.bugalu.nlp.service;

import java.util.List;

public interface NLPService {

	public List<String> retrievePhrases(String text); 
	public String computeSentiment(String text); 

}

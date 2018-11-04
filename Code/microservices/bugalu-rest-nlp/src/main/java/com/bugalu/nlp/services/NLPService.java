package com.bugalu.nlp.services;

import java.util.List;

public interface NLPService {

	public List<String> retrievePhrases(String text); 
	public String computeSentiment(String text); 

}

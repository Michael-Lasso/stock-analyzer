package com.in28minutes.microservices.currencyexchangeservice;

import java.util.List;

public interface NLPService {

	public List<String> retrievePhrases(String text); 
	public String computeSentiment(String text); 

}

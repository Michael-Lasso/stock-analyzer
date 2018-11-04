package com.bugalu.nlp.services;

import java.util.List;

import com.bugalu.nlp.domain.Business;
import com.bugalu.nlp.domain.Recommendation;

public interface RecommendationService {

	public List<Recommendation> processRequest(List<Business> business);
}

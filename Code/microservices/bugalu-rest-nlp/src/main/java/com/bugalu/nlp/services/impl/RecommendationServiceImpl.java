package com.bugalu.nlp.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugalu.nlp.domain.Business;
import com.bugalu.nlp.domain.Keyword;
import com.bugalu.nlp.domain.Recommendation;
import com.bugalu.nlp.domain.Review;
import com.bugalu.nlp.services.NLPService;
import com.bugalu.nlp.services.RecommendationService;
import com.bugalu.nlp.utils.PredefinedCategories;

@Service
public class RecommendationServiceImpl implements RecommendationService {

	private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

	@Autowired
	private NLPService nlp;

	public List<Recommendation> processRequest(List<Business> businesses) {
		List<Recommendation> recommendations = new ArrayList<>();
		for (Business business : businesses) {
			Recommendation r = Recommendation.factory(business);
			List<Keyword> keywords = new ArrayList<>();

			for (Review review : business.getReviews()) {
				String text = review.getRev_text();
				final String sentiment = nlp.computeSentiment(text);

				List<String> phrases = nlp.retrievePhrases(text);
				keywords.addAll(phrases.stream().map(a -> new Keyword(a, sentiment)).collect(Collectors.toList()));
			}
			r.setKeywords(combineWords(keywords));
			recommendations.add(r);
		}
		return recommendations;
	}

	private List<Keyword> combineWords(List<Keyword> keywords) {
		Map<String, Set<String>> set = PredefinedCategories.categories;
		Map<String, Keyword> result2 = new HashMap<>();

		for (String setKey : set.keySet()) {
			Set<String> wordSet = set.get(setKey);
			for (Keyword keyword : keywords) {
				boolean flag = wordSet.stream().anyMatch(a -> keyword.getName().contains(a));
				if (flag) {
					keyword.setName(setKey);
					if (result2.containsKey(keyword.getName())) {
						Keyword keyword2 = result2.get(keyword.getName());
						keyword2.combine(keyword);
					} else {
						result2.put(keyword.getName(), keyword);
					}
				}
			}
		}
		return new ArrayList<>(result2.values());
	}
}

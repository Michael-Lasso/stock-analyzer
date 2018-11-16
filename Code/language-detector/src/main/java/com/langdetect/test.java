package com.langdetect;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import com.langdetect.ngram.NgramExtractors;
import com.langdetect.profiles.LanguageProfileReader;

public class test {
	public static void main(String[] args) throws IllegalStateException, IOException {
		LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
				.shortTextAlgorithm(0).withProfiles(new LanguageProfileReader().readAllBuiltIn()).build();
		Map<String, Double> map = languageDetector.getProbabilities("").stream()
				.collect(Collectors.toMap(x -> x.getLocale().getLanguage(), x -> x.getProbability()));
	}
}

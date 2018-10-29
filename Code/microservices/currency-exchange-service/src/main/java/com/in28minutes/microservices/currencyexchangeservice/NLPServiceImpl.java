package com.in28minutes.microservices.currencyexchangeservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesRequest;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesResult;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;

@Service
public class NLPServiceImpl implements NLPService {

	private static final Logger log = LoggerFactory.getLogger(NLPServiceImpl.class);

	private AWSCredentialsProvider awsCreds;
	private AmazonComprehend comprehendClient;

	
	@Value("${access.key}")
	private String access_key;

	@Value("${secret.key}")
	private String secret_key;

	@PostConstruct
	public void init() {
		try {
			System.setProperty("aws.accessKeyId", access_key);
			System.setProperty("aws.secretKey", secret_key);
			awsCreds = DefaultAWSCredentialsProviderChain.getInstance();
			comprehendClient = AmazonComprehendClientBuilder.standard().withCredentials(awsCreds)
					.withRegion(Regions.US_EAST_1).build();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public List<String> retrievePhrases(String text) {
		if (text == null || text.length() < 2) {
			return new ArrayList<String>();
		}

		DetectKeyPhrasesRequest detectKeyPhrasesRequest = new DetectKeyPhrasesRequest().withText(text)
				.withLanguageCode("en");
		DetectKeyPhrasesResult detectKeyPhrasesResult = comprehendClient.detectKeyPhrases(detectKeyPhrasesRequest);

		List<String> result = detectKeyPhrasesResult.getKeyPhrases().stream().map(a -> a.getText())
				.collect(Collectors.toList());

		return result;
	}

	public String computeSentiment(String text) {

		if (text == null || text.length() < 2) {
			return Sentiment.NEUTRAL.toString();
		}

		DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest().withText(text)
				.withLanguageCode("en");
		DetectSentimentResult detectSentimentResult = comprehendClient.detectSentiment(detectSentimentRequest);

		String sentiment = detectSentimentResult.getSentiment();
		if (sentiment == null || sentiment.equals("null") || sentiment.length() < 1) {
			log.info("wrong sentiment: " + sentiment + " - " + text);
		}
		return sentiment;
	}

}

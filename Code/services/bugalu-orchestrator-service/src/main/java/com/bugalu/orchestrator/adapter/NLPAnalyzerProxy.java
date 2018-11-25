package com.bugalu.orchestrator.adapter;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bugalu.domain.twitter.Sentiment;
import com.bugalu.domain.twitter.Twit;

@RibbonClient(name = "bugalu-nlp-analyzer-service")
@FeignClient(name = "bugalu-api-gateway-server")
public interface NLPAnalyzerProxy {

	@PostMapping("bugalu-nlp-analyzer-service/sentiments")
	public ResponseEntity<Twit> computSentimentValue(@RequestBody Twit twit);

	@PostMapping("bugalu-nlp-analyzer-service/sentiments")
	public ResponseEntity<String> computSentimentValue2(@RequestBody Twit twit);
	
	@PostMapping("bugalu-nlp-analyzer-service/sentiments/text")
	public ResponseEntity<Sentiment> computSentimentValue2(@RequestBody String twit);
}

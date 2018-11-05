package com.bugalu.orchestrator.adapter;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bugalu.orchestrator.domain.Twit;

@RibbonClient(name = "bugalu-nlp-analyzer-service")
@FeignClient(name = "bugalu-api-gateway-server")
public interface TwitterProxy {

	@PostMapping("bugalu-nlp-analyzer-service/sentiments")
	public ResponseEntity<Twit> computSentimentValue(@RequestBody Twit twit);
}

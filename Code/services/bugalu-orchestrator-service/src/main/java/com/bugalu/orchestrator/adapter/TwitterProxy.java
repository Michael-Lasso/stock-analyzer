package com.bugalu.orchestrator.adapter;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bugalu.domain.twitter.Twit;

@RibbonClient(name = "bugalu-twitter-streaming-service")
@FeignClient(name = "bugalu-api-gateway-server")
public interface TwitterProxy {

	@GetMapping("bugalu-twitter-streaming-service/twits")
	public ResponseEntity<List<Twit>> getAllTwits();

	@PostMapping("bugalu-twitter-streaming-service/twits/reset")
	public ResponseEntity<Boolean> clearTwitsById(@RequestBody List<String> idList);
}

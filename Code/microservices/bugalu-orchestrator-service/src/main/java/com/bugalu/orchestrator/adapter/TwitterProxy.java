package com.bugalu.orchestrator.adapter;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bugalu.orchestrator.domain.Twit;

@RibbonClient(name = "bugalu-twitter-streaming-service")
@FeignClient(name = "bugalu-api-gateway-server")
public interface TwitterProxy {

	@PostMapping("bugalu-twitter-streaming-service/twits")
	public ResponseEntity<List<Twit>> getAllTwits();

	@PostMapping("bugalu-twitter-streaming-service/twits/reset")
	public ResponseEntity<Twit> clearTwitsById(@RequestBody List<String> idList);
}

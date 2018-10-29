package com.bugalu.adapter;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bugalu.domain.Twit;

@RibbonClient(name = "currency-exchange-service")
@FeignClient(name = "netflix-api-zuul-gateway-server")
public interface CurrencyExchangeServiceProxy {

	@PostMapping("currency-exchange-service/sentiment")
	public ResponseEntity<Twit> computSentimentValue(@RequestBody Twit twit);
}

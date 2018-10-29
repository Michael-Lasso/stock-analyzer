package com.bugalu.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.bugalu.twitter.adapter")
@EnableDiscoveryClient
public class TwitterFeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterFeedApplication.class, args);
	}
}

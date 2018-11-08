package com.bugalu.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.bugalu.twitter.adapter")
@EnableDiscoveryClient
@SpringBootApplication
public class TwitterStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterStreamingApplication.class, args);
	}
}

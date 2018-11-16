package com.bugalu.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StockStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockStreamingApplication.class, args);
	}
}

package com.bugalu.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDiscoveryClient
@SpringBootApplication
public class StockStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockStreamingApplication.class, args);
	}
}

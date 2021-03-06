package com.bugalu.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubApplication.class, args);
	}

}

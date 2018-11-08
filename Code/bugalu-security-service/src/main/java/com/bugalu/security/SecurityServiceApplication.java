package com.bugalu.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "security.token")
	public TokenProperties tokenProperties() {
		return new TokenProperties();
	}

	@Bean
	public PasswordEncoder passwordEncoder(@Value("${security.password.strength:10}") int strength) {
		return new BCryptPasswordEncoder(strength);
	}

}

package com.bugalu.orchestrator.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
// @ConditionalOnProperty(prefix = "com.redrunner.controller", name = "traffic")
public class RandomTraffic {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Scheduled(cron = "${orchestrator.schedule}")
	public void reportCurrentTime() {
		Random rand = new Random();
	}
}

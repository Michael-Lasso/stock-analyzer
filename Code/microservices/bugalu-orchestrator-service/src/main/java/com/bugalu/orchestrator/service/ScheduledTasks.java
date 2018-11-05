package com.bugalu.orchestrator.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Scheduled(fixedRate = 20000)
	public void reportCurrentTime() {

	}

}

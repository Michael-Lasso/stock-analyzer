package com.bugalu.orchestrator.service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bugalu.orchestrator.domain.Twit;

@Component
public class OrchestratorService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private final RestParallelClientService service;

	public OrchestratorService(RestParallelClientService service) {
		this.service = service;
	}

	// @Scheduled(cron = "${orchestrator.schedule}")
	public void reportCurrentTime() {
		log.info("running: {}", new Date());
		Future<List<Twit>> twits = service.getAllTwits();
		try {
			List<Twit> list = twits.get();
			log.info("fetches list");
			List<String> idList = list.stream().map(a -> a.getTopic()).collect(Collectors.toList());
			Future<Boolean> isClear = service.clearTwits(idList);
			log.info("reseted: {}", isClear.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random rand = new Random();
	}
}

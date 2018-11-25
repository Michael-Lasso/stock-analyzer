package com.bugalu.twitter.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bugalu.domain.stock.TopScores;
import com.bugalu.domain.twitter.FutureTwit;
import com.bugalu.domain.twitter.Language;
import com.bugalu.domain.twitter.Twit;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

@Component
public class TwitterServiceImpl implements TwitterService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${twitter.consumer.key}")
	String consumerKey;
	@Value("${twitter.consumer.secret}")
	String consumerSecret;
	@Value("${twitter.token}")
	String token;
	@Value("${twitter.secret}")
	String secret;
	@Value("${message.queue.size:1000}")
	int msgQueSize;
//	@Value("${language.filter}")
	String lan = "es";
//List<String> terms = Lists.newArrayList("tesla", "@elonmusk");
	List<String> terms = Lists.newArrayList("@IvanDuque");
	private String stockRelated = "TSLA";
	//TODO add this as a yaml property
	private static final int LIST_INCREMENTS = 10;

	private Thread thread;
	private ConcurrentHashMap<String, Twit> map;
	private static JsonParser jsonParser = new JsonParser();
	private ConcurrentLinkedQueue<FutureTwit> twitterFutureQueue;
	private final ConcurrentRestClient concurrentRestClient;
	private TwitterFilterService twitterFilterService;

	@Autowired
	public TwitterServiceImpl(ConcurrentRestClient concurrentRestClient) {
		this.concurrentRestClient = concurrentRestClient;
	}

	private Twit extractIdFromTweet(String tweetJson) {
		String text = jsonParser.parse(tweetJson).getAsJsonObject().get("full_text").getAsString();
		String id = jsonParser.parse(tweetJson).getAsJsonObject().get("id_str").getAsString();
		JsonObject countWeightObj = jsonParser.parse(tweetJson).getAsJsonObject().getAsJsonObject("user");
		String countWeight = countWeightObj.get("followers_count").getAsString();
		Twit twit = new Twit();
		twit.setCountWeight(Integer.parseInt(countWeight));
		twit.setText(text.replaceAll("[\\r\\n]+", ""));
		twit.setId(id);
		twit.setTerms(terms);
		twit.setStockRelated(stockRelated);
		log.info(tweetJson);
		// TODO change to retrieve date from tweetJson, works for now
		twit.setDateCreated(new Date());
		return twit;
	}

	public List<Twit> poll() {
		List<Twit> result = map
				.values()
				.stream()
				.sorted(Comparator.comparing(Twit::getDateCreated))
				.limit(LIST_INCREMENTS)
				.collect(Collectors.toList());
		return result;
	}

	public boolean clearTwitsById(List<String> idList) {
		log.info("Initial twit size before deletion: {}, and removing: {}", map.size(), idList.size());
		for (String key : idList) {
			map.remove(key);
		}
		log.info("New current size: {}", map.size());
		return Boolean.TRUE;
	}

	@PostConstruct
	public void init() {
		map = new ConcurrentHashMap<>();
		twitterFutureQueue = new ConcurrentLinkedQueue<>();
		Runnable runnable = () -> {
			run();
		};
		twitterFilterService = new TwitterFilterService(map, twitterFutureQueue);
		thread = new Thread(runnable);
		thread.start();
		twitterFilterService.start();
	}

	public void run() {

		log.info("Setup");

		/**
		 * Set up your blocking queues: Be sure to size these properly based on expected
		 * TPS of your stream
		 */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(msgQueSize);

		Client client = createTwitterClient(msgQueue);
		client.connect();

		// add a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("shutting down client from twitter...");
			client.stop();
		}));

		while (!client.isDone()) {
			String msg = null;
			try {
				msg = msgQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				client.stop();
			}
			if (msg != null) {
				
				Twit twit = extractIdFromTweet(msg);
				Language language = new Language(twit.getText(), lan);
				twitterFutureQueue.add(new FutureTwit(twit, concurrentRestClient.getTextLanguage(language)));
			}
		}
	}

	public Client createTwitterClient(BlockingQueue<String> msgQueue) {

		Hosts curatorHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint curatorEndpoint = new StatusesFilterEndpoint();

		curatorEndpoint.trackTerms(terms);

		Authentication twitterCurator = new OAuth1(consumerKey, consumerSecret, token, secret);

		ClientBuilder builder = new ClientBuilder().name("twitter-curator-Client-01").hosts(curatorHosts)
				.authentication(twitterCurator).endpoint(curatorEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));

		Client curatorClient = builder.build();
		return curatorClient;
	}

	@Override
	public Twit getTwit(String twit_id) {
		return map.get(twit_id);
	}

}

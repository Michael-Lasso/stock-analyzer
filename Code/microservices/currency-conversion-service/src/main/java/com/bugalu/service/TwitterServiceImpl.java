package com.bugalu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bugalu.domain.Twit;
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

@Service
public class TwitterServiceImpl implements TwitterService {

	private ConcurrentHashMap<String, Twit> map;
	private static JsonParser jsonParser = new JsonParser();

	private static Twit extractIdFromTweet(String tweetJson) {
		String text = jsonParser.parse(tweetJson).getAsJsonObject().get("text").getAsString();
		String topic = jsonParser.parse(tweetJson).getAsJsonObject().get("id_str").getAsString();
		JsonObject countWeightObj = jsonParser.parse(tweetJson).getAsJsonObject().getAsJsonObject("user");
		String countWeight = countWeightObj.get("followers_count").getAsString();
		Twit twit = new Twit();
		twit.setCountWeight(Integer.parseInt(countWeight));
		twit.setText(text);
		twit.setTopic(topic);
		return twit;
	}

	public List<Twit> poll() {
		return new ArrayList<>(map.values());
	}

	private Thread thread;

	@PostConstruct
	public void init() {
		map = new ConcurrentHashMap<String, Twit>();
		Runnable runnable = () -> {
			run();
		};
		thread = new Thread(runnable);
		thread.start();
	}

	Logger logger = LoggerFactory.getLogger(TwitterServiceImpl.class.getName());

	// use your own credentials - don't share them with anyone
	String consumerKey = "wn3JdP6PMFomWT3mEdSkAtkWc";
	String consumerSecret = "A1e4hiYYwgqvQ9IwIxVUurzhLG0d9pQvhEgNOnuL2ELgsZZ6a2";
	String token = "1048599854887460864-csGwXHFNPQz0NTvruHjrHhhd7u0DfQ";
	String secret = "h8xwLXFGmfOOOEjViktEyBftLeRSmyvLHOFtTsv2RxCCx";

	List<String> terms = Lists.newArrayList("tesla", "", "model 3", "elon_musk");

	public TwitterServiceImpl() {
	}

	public void run() {

		logger.info("Setup");

		/**
		 * Set up your blocking queues: Be sure to size these properly based on expected
		 * TPS of your stream
		 */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);

		// create a twitter client
		Client client = createTwitterClient(msgQueue);
		// Attempts to establish a connection.
		client.connect();

		// create a kafka producer
		// KafkaProducer<String, String> producer = createKafkaProducer();

		// add a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("stopping application...");
			logger.info("shutting down client from twitter...");
			client.stop();
			logger.info("closing producer...");
			// producer.close();
			logger.info("done!");
		}));

		// loop to send tweets to kafka
		// on a different thread, or multiple different threads....
		while (!client.isDone()) {
			String msg = null;
			try {
				msg = msgQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				client.stop();
			}
			if (msg != null) {
//				logger.info("Twit {}", msg);
				Twit twit = extractIdFromTweet(msg);
				map.put(twit.getTopic(), twit);
			}
		}
		logger.info("End of application");
	}

	public Client createTwitterClient(BlockingQueue<String> msgQueue) {

		/**
		 * Declare the host you want to connect to, the endpoint, and authentication
		 * (basic auth or oauth)
		 */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

		hosebirdEndpoint.trackTerms(terms);

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

		ClientBuilder builder = new ClientBuilder().name("Hosebird-Client-01") // optional: mainly for the logs
				.hosts(hosebirdHosts).authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));

		Client hosebirdClient = builder.build();
		return hosebirdClient;
	}

	@Override
	public Twit getTwit(String twit_id) {
		return map.get(twit_id);
	}

}

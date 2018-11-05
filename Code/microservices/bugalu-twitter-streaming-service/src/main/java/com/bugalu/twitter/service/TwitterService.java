package com.bugalu.twitter.service;

import java.util.List;

import com.bugalu.twitter.domain.Twit;

public interface TwitterService {

	List<Twit> poll();

	Twit getTwit(String twit_id);

	boolean clearTwitsById(List<String> ListId);
}

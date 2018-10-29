package com.bugalu.service;

import java.util.List;

import com.bugalu.domain.Twit;

public interface TwitterService {

	List<Twit> poll();

	Twit getTwit(String twit_id);
}

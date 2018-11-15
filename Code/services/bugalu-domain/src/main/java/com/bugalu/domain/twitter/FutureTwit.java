package com.bugalu.domain.twitter;

import java.util.concurrent.Future;

import com.bugalu.domain.twitter.Twit;

public class FutureTwit {

	private Twit twit;
	private Future<Boolean> languageFlag;

	public FutureTwit(Twit twit, Future<Boolean> languageFlag) {
		this.twit = twit;
		this.languageFlag = languageFlag;
	}

	public Twit getTwit() {
		return twit;
	}

	public Future<Boolean> getLanguageFlag() {
		return languageFlag;
	}

}

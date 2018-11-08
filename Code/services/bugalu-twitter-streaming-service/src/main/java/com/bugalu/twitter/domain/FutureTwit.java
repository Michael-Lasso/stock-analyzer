package com.bugalu.twitter.domain;

import java.util.concurrent.Future;

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

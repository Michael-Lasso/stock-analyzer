package com.bugalu.domain.twitter;

import java.util.Map;

public interface SocialMedia {

	Map<String, Stats> computeData();

	String getKey();

	boolean isUndefined();

	Post getPostBody();

	String getId();

	String getPostText();

	long getPostScore();

}

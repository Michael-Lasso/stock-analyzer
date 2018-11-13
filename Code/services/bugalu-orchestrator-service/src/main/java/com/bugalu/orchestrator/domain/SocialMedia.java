package com.bugalu.orchestrator.domain;

import java.util.Map;

public interface SocialMedia {

	Map<String, Stats> computeData();

	String getKey();

	boolean isUndefined();

}

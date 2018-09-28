package com.tvd12.ezyfoxserver.service.impl;

import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;

public class EzySimpleSessionTokenGenerator implements EzySessionTokenGenerator {

	private final AtomicLong counter;
	private final String serverNodeName;
	
	public EzySimpleSessionTokenGenerator() {
	    this("ezyfox");
	}
	
	public EzySimpleSessionTokenGenerator(String serverNodeName) {
	    this.counter = new AtomicLong();
	    this.serverNodeName = serverNodeName;
	}
	
	@Override
	public String generate() {
	    String token = serverNodeName + "#" + getCount();
	    return token;
	}
	
	private long getCount() {
		return counter.incrementAndGet();
	}
	
}

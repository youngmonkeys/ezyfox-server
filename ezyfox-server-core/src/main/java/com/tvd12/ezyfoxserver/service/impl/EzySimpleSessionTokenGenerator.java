package com.tvd12.ezyfoxserver.service.impl;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfox.sercurity.EzyBase64;
import com.tvd12.ezyfox.sercurity.EzyUuid;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;

public class EzySimpleSessionTokenGenerator implements EzySessionTokenGenerator {

	private AtomicLong counter = new AtomicLong();
	
	@Override
	public String generate() {
		return EzyBase64.encodeUtf(getRawString());
	}
	
	private String getRawString() {
		return getCurrentTime() + "-" + randomUuid() + "-" + getCount();
	}
	
	private UUID randomUuid() {
		return EzyUuid.random();
	}
	
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
	private long getCount() {
		return counter.incrementAndGet();
	}
	
}

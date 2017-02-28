package com.tvd12.ezyfoxserver.service.impl;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfoxserver.codec.EzyBase64;
import com.tvd12.ezyfoxserver.codec.EzyUuid;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGeneration;

public class EzySessionTokenGenerationImpl implements EzySessionTokenGeneration {

	private AtomicLong counter;
	
	{
		counter = new AtomicLong();
	}
	
	@Override
	public String generate() {
		return EzyBase64.encodeUTF(getRawString());
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

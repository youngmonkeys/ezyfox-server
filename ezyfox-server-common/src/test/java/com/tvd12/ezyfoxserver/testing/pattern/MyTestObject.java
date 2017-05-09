package com.tvd12.ezyfoxserver.testing.pattern;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

public class MyTestObject {

	@Getter
	private int id;
	
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	
	public MyTestObject() {
		this(ID_GENERATOR.incrementAndGet());
	}
	
	public MyTestObject(int id) {
		this.id = id;
	}
	
}

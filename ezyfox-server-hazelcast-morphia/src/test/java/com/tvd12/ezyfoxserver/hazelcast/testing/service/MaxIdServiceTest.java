package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;

public class MaxIdServiceTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		MAX_ID_SERVICE.incrementAndGet("count");
		long time = System.currentTimeMillis();
		long newId = 0;
		for(int i = 0 ; i < 10000 ; i++)
			newId = MAX_ID_SERVICE.incrementAndGet("count");
		long offset = System.currentTimeMillis() - time;
		System.err.println("elapsed = " + offset + ", newId = " + newId);
		Thread.sleep(5000L);
	}
	
}

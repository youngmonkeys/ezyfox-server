package com.tvd12.ezyfoxserver.databridge.test;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyTime;

public class EzyProxyTimeTest {

	@Test
	public void test() {
		long timeMillis = System.currentTimeMillis();
		EzyProxyTime time = new EzyProxyTime(timeMillis);
		assert time.getMillis() == timeMillis;
		System.out.println(time.getTimestamp());
	}
	
}

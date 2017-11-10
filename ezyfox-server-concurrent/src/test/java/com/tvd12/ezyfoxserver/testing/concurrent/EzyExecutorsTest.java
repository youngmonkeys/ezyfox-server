package com.tvd12.ezyfoxserver.testing.concurrent;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.test.base.BaseTest;

public class EzyExecutorsTest extends BaseTest {

	@Test
	public void test() {
		EzyExecutors.newFixedThreadPool(5, "test");
		EzyExecutors.newScheduledThreadPool(5, "test");
		EzyExecutors.newSingleThreadExecutor("test");
		EzyExecutors.newSingleThreadScheduledExecutor("test");
		EzyExecutors.newThreadFactory("test", false);
		EzyExecutors.newThreadFactory("test", Thread.NORM_PRIORITY);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyExecutors.class;
	}
	
}

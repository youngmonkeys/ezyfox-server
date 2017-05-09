package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyTimes;
import com.tvd12.test.base.BaseTest;
import static org.testng.Assert.*;

public class EzyTimesTest extends BaseTest {

	@Test
	public void test() {
		assertEquals(EzyTimes.getRemainTime(0, 0), 0);
		assertTrue(EzyTimes.getRemainTime(100, System.currentTimeMillis()) <= 100);
		
		assertEquals(EzyTimes.getPositiveRemainTime(0, 0), 0);
		assertTrue(EzyTimes.getPositiveRemainTime(100, System.currentTimeMillis()) <= 100);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyTimes.class;
	}
	
}

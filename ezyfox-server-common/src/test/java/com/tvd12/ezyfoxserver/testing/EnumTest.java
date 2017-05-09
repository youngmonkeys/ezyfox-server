package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class EnumTest extends BaseTest {

	@SuppressWarnings("unused")
	@Test
	public void test() {
		long time = Performance.create()
			.loop(1000000)
			.test(() -> {
				One.ABC.getClass().isEnum();
				One one = Enum.valueOf(One.class, "ABC");
			})
			.getTime();
		System.err.println("loop time is: " + time);
	}
	
	public static enum One {
		ABC,DEF
	}
	
}

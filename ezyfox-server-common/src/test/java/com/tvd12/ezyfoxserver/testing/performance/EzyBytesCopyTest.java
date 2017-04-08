package com.tvd12.ezyfoxserver.testing.performance;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.test.performance.Performance;

public class EzyBytesCopyTest {

	@Test
	public void test1() {
		byte[][] bytess = new byte[][] {
			{1,2},{3,4}
		};
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			EzyBytes.copy(bytess);
		})
		.getTime();
		System.out.println("test1 elapsed time = " + time);
	}
	
}

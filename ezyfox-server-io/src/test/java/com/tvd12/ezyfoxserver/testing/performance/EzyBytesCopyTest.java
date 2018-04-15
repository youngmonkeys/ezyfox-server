package com.tvd12.ezyfoxserver.testing.performance;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.test.performance.Performance;

public class EzyBytesCopyTest {

	@Test
	public void test1() {
		byte[][] bytess = new byte[][] {
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4}
		};
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			EzyBytes.merge(bytess);
		})
		.getTime();
		System.out.println("test1 elapsed time = " + time);
	}
	
	@Test
	public void test2() {
		byte[][] bytess = new byte[][] {
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4},
			{1,2},{3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4}
		};
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			int passed = 0;
			byte[] bytes = new byte[EzyBytes.totalBytes(bytess)];
			for(int i = 0 ; i < bytess.length ; i++) {
				System.arraycopy(bytess[i], 0, bytes, passed, bytess[i].length);
				passed += bytess[i].length;
			}
		})
		.getTime();
		System.out.println("test2 elapsed time = " + time);
	}
	
}

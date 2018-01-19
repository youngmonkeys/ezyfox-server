package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.io.EzyArrays;
import com.tvd12.ezyfoxserver.io.EzyByteBuffers;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class EzyArraysTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyArrays.class;
	}
	
	@Test
	public void test() {
		Collection<String> coll = Lists.newArrayList("1", "2", "3");
		Long[] array = EzyArrays.newArray(coll, 
				(s) -> new Long[s], 
				s -> Long.valueOf(s));
		Assert.assertEquals(array, new Long[] {1L, 2L, 3L});
	}
	
	@Test
	public void test1() {
		assert EzyArrays.min(new Long[] {3L,1L,2L}) == 1L;
		assert EzyArrays.max(new Long[] {3L,1L,5L,6L,2L}) == 6L;
	}
	
	@Test
	public void test2() {
		Collection<Long> coll = Lists.newArrayList(1L, 2L, 3L);
		Long[] array = EzyArrays.newArray(coll, 
				(s) -> new Long[s]);
		Assert.assertEquals(array, new Long[] {1L, 2L, 3L});
	}
	
	@Test
	public void test3() {
		byte[] bytes = new byte[2000];
		for(int i = 0 ; i < bytes.length ; i++)
			bytes[i] = 2;
		byte[] bytes1 = new byte[bytes.length + 1];
		bytes1[0] = 1;
		System.arraycopy(bytes, 0, bytes1, 1, bytes.length);
		byte[] bytes2 = EzyBytes.merge((byte)1, bytes);
		assertEquals(bytes1, bytes2);
	}
	
	@Test
	public void test5() {
		System.out.println(Arrays.toString(EzyBytes.merge((byte)1, new byte[] {3, 6, 2, 5})));
	}
	
	@Test
	public void mergePerformaceTest() {
		byte[] bytes = new byte[20000];
		long time1 = Performance.create()
				.test(() -> {
					EzyBytes.merge((byte)1, bytes);
				})
				.getTime();
		long time2 = Performance.create()
				.test(() -> {
					EzyByteBuffers.merge2bytes((byte)1, bytes);
				})
				.getTime();
		System.out.println("mergePerformaceTest.time1 = " + time1);
		System.out.println("mergePerformaceTest.time2 = " + time2);
	}
}

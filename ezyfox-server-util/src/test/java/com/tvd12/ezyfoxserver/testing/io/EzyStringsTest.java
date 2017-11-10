package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.test.base.BaseTest;

public class EzyStringsTest extends BaseTest {

	@Test
	public void test() throws Exception {
		assertEquals("abc", EzyStrings.newUtf("abc".getBytes("UTF-8")));
		assertEquals("abc", EzyStrings.newUtf(ByteBuffer.wrap("abc".getBytes("UTF-8")), 3));
		assertEquals(EzyStrings.getUtfBytes("abc"), "abc".getBytes("UTF-8"));
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() {
		EzyStrings.newString(new byte[] {1, 2, 3}, "kkk");
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzyStrings.getBytes("abc", "hhh");
	}
	
	@Test
	public void test3() {
		String[] array = new String[] {"1", "2", "3"};
		assert EzyStrings.getString(array, 1, "x").equals("2");
		assert EzyStrings.getString(array, 100, "x").equals("x");
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyStrings.class;
	}
	
}

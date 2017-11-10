package com.tvd12.ezyfoxserver.testing.concurrent;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.concurrent.EzyLazyInitializer;
import com.tvd12.test.base.BaseTest;

public class EzyLazyInitializerTest extends BaseTest {

	@Test(expectedExceptions = {IllegalStateException.class})
	public void test() {
		new EzyLazyInitializer<String>(()-> new String()) {
			@Override
			protected String doGet() throws ConcurrentException {
				throw new ConcurrentException(new Exception());
			}
		}.get();
	}
	
	@Test
	public void test1() {
		String str = new EzyLazyInitializer<>(() -> new String("abc")).get();
		assert str.equals("abc");
	}
}

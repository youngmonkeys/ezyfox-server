package com.tvd12.ezyfoxserver.testing.stream;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.stream.EzyClassPathInputStreamLoader;
import com.tvd12.test.base.BaseTest;

public class EzyClassPathInputStreamLoaderTest extends BaseTest {

	@Test
	public void test1() {
		assert EzyClassPathInputStreamLoader.builder()
			.context(getClass())
			.build()
			.load("fasfasdf.afdsf") == null;
	}
	
}

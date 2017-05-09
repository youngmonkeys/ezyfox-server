package com.tvd12.ezyfoxserver.testing.file;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzyAnywayFileFetcher;
import com.tvd12.test.base.BaseTest;

public class EzyAnywayFileFetcherTest extends BaseTest {

	@Test
	public void test1() {
		File file = EzyAnywayFileFetcher.builder()
				.classLoader(getClass().getClassLoader())
				.context(getClass())
				.build()
				.get("AllTests.tng.xml");
		assert file != null;
	}
	
	@Test
	public void test2() {
		File file = EzyAnywayFileFetcher.builder()
				.classLoader(getClass().getClassLoader())
				.context(getClass())
				.build()
				.get("pom.xml");
		assert file != null;
	}
	
}

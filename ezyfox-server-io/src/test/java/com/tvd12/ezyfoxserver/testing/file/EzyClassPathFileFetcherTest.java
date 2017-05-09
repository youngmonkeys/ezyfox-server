package com.tvd12.ezyfoxserver.testing.file;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzyClassPathFileFetcher;
import com.tvd12.ezyfoxserver.file.EzyFileFetcher;
import com.tvd12.test.base.BaseTest;

public class EzyClassPathFileFetcherTest extends BaseTest {

	@Test
	public void test1() {
		File file = EzyClassPathFileFetcher.builder()
				.classLoader(getClass().getClassLoader())
				.context(getClass())
				.build()
				.get("AllTests.tng.xml");
		assert file != null;
	}
	
	@Test
	public void test2() {
		File file = new EzyClassPathFileFetcher.Builder() {
			public EzyFileFetcher build() {
				return new EzyClassPathFileFetcher(this) {
					protected File firstGet(String filePath) {
						return null;
					}
				};
				
			}
			}
				.classLoader(getClass().getClassLoader())
				.context(getClass())
				.build()
				.get("AllTests.tng.xml");
		assert file == null;
	}
	
	@Test
	public void test3() {
		File file = EzyClassPathFileFetcher.builder()
				.classLoader(getClass().getClassLoader())
				.context(getClass())
				.build()
				.get("safsdf.tng.xml");
		assert file == null;
	}
	
}

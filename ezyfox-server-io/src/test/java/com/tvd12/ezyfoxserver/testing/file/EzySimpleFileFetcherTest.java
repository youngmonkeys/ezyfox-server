package com.tvd12.ezyfoxserver.testing.file;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyFileNotFoundException;
import com.tvd12.ezyfoxserver.file.EzySimpleFileFetcher;
import com.tvd12.test.base.BaseTest;

public class EzySimpleFileFetcherTest extends BaseTest {

	@Test(expectedExceptions = {EzyFileNotFoundException.class})
	public void test1() {
		EzySimpleFileFetcher.builder().build().get("fsadfsaf.csdf");
	}
	
}

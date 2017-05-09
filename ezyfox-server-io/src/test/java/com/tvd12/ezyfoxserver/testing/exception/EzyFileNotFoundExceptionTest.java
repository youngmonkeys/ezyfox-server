package com.tvd12.ezyfoxserver.testing.exception;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyFileNotFoundException;
import com.tvd12.test.base.BaseTest;

public class EzyFileNotFoundExceptionTest extends BaseTest {

	@Test(expectedExceptions = {EzyFileNotFoundException.class})
	public void test1() {
		throw new EzyFileNotFoundException(new Exception());
	}
	
	@Test(expectedExceptions = {EzyFileNotFoundException.class})
	public void test2() {
		throw new EzyFileNotFoundException(new Exception().toString());
	}
	
	@Test(expectedExceptions = {EzyFileNotFoundException.class})
	public void test3() {
		throw new EzyFileNotFoundException("", new Exception());
	}
	
}

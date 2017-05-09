package com.tvd12.ezyfoxserver.testing.exception;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyNegativeValueException;
import com.tvd12.test.base.BaseTest;

public class EzyNegativeValueExceptionTest extends BaseTest {

	@Test
	public void test() {
		new EzyNegativeValueException(-10);
	}
	
}

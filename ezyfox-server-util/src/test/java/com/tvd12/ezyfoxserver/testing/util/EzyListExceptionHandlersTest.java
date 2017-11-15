package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyListExceptionHandlers;
import com.tvd12.test.base.BaseTest;

public class EzyListExceptionHandlersTest extends BaseTest {

	@Test
	public void test() {
		EzyExceptionHandlers handlers = new EzyListExceptionHandlers();
		handlers.addExceptionHandler(new EzyExceptionHandler() {
			@Override
			public void handleException(Thread thread, Throwable throwable) {
			}
		});
		handlers.handleException(Thread.currentThread(), new Exception());
	}
	
}

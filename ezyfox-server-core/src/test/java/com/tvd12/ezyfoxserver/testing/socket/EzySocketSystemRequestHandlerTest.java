package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketSystemRequestHandlerTest {

	@Test
	public void test() {
	    EzySocketSystemRequestHandler handler = new EzySocketSystemRequestHandler();
		MethodInvoker.create()
		    .object(handler)
		    .method("getRequestType")
		    .invoke();
	}
	
}

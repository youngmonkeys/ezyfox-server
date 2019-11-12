package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketExtensionRequestHandlerTest {

	@Test
	public void test() {
		EzySocketExtensionRequestHandler handler = new EzySocketExtensionRequestHandler();
		MethodInvoker.create()
		    .object(handler)
		    .method("getRequestType")
		    .invoke();
	}
	
}

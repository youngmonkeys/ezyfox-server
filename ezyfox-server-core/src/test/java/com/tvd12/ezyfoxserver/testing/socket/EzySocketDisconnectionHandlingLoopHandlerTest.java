package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketDisconnectionHandlingLoopHandlerTest {

	@Test
	public void test() {
		EzySocketDisconnectionHandlingLoopHandler handler = new EzySocketDisconnectionHandlingLoopHandler();
		MethodInvoker.create()
		    .object(handler)
		    .method("getThreadName")
		    .invoke();
	}
	
}

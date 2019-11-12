package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketUserRemovalHandlingLoopHandlerTest {

	@Test
	public void test() {
	    EzySocketUserRemovalHandlingLoopHandler handler = new EzySocketUserRemovalHandlingLoopHandler();
		MethodInvoker.create()
		    .object(handler)
		    .method("getThreadName")
		    .invoke();
	}
	
}

package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketWritingLoopHandlerTest {

	@Test
	public void test() {
	    EzySocketWritingLoopHandler handler = new EzySocketWritingLoopHandler();
		MethodInvoker.create()
		    .object(handler)
		    .method("getThreadName")
		    .invoke();
	}
	
}

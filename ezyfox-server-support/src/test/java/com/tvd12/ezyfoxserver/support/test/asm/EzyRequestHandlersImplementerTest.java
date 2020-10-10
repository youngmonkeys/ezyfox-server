package com.tvd12.ezyfoxserver.support.test.asm;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserSessionEvent;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.asm.EzyAsmRequestHandler;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlerImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlersImplementer;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController2;
import com.tvd12.ezyfoxserver.support.test.data.GreetRequest;

public class EzyRequestHandlersImplementerTest {

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void test() {
		EzyAppContext context = mock(EzyAppContext.class);
		EzySession session = mock(EzyAbstractSession.class);
		EzyUser user = new EzySimpleUser();
		EzyUserSessionEvent event = new EzySimpleUserSessionEvent(user, session);
		EzyRequestHandlerImplementer.setDebug(true);
		EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
		Map<String, EzyUserRequestHandler> handlers = implementer.implement(new HelloController());
		for(EzyUserRequestHandler handler : handlers.values()) {
			handler.handle(context, event, new GreetRequest("Dzung"));
		}
		EzyRequestHandlerImplementer.setDebug(false);
		implementer = new EzyRequestHandlersImplementer();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void testImplementFailedCase() {
		EzyRequestControllerProxy proxy 
			= new EzyRequestControllerProxy(new HelloController());
		EzyRequestHandlerImplementer implementer = 
				new EzyRequestHandlerImplementer(
						proxy, 
						proxy.getRequestHandlerMethods().get(0)) {
			@Override
			protected EzyAsmRequestHandler doimplement() throws Exception {
				throw new RuntimeException("test");
			}
		};
		implementer.implement();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void testImplementFailedCase2() {
		EzyRequestHandlerImplementer.setDebug(true);
		EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
		implementer.implement(new HelloController2());
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void testImplementFailedCase3() {
		EzyRequestHandlerImplementer.setDebug(true);
		EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
		implementer.implement(Arrays.asList(new HelloController(), new HelloController()));
	}
	
}

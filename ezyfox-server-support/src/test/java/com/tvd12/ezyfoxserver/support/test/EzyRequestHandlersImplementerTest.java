package com.tvd12.ezyfoxserver.support.test;

import java.util.Map;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserSessionEvent;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlerImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlersImplementer;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController;
import com.tvd12.ezyfoxserver.support.test.data.GreetRequest;
import static org.mockito.Mockito.*;

public class EzyRequestHandlersImplementerTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
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
	}
	
}

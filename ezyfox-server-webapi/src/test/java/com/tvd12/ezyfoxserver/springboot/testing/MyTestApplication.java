package com.tvd12.ezyfoxserver.springboot.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

public class MyTestApplication {

	public EzyServerContext newServerContext() {
		EzySimpleServer server = new MyTestServer();
		EzyServerContext context = mock(EzyServerContext.class);
		when(context.getServer()).thenReturn(server);
		return context;
	}

}

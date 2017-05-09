package com.tvd12.ezyfoxserver.springboot.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

@SpringBootApplication
public class MyTestApplication {

	@Bean
    public EzyServerContext newServerContext() {
    	EzySimpleServer server = new MyTestServer();
    	EzyServerContext context = mock(EzyServerContext.class);
    	when(context.getServer()).thenReturn(server);
    	return context;
    }
	
}

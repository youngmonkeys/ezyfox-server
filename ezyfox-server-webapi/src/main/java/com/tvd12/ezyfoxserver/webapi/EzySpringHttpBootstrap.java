package com.tvd12.ezyfoxserver.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tvd12.ezyfoxserver.EzyHttpBootstrap;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@SpringBootApplication
public class EzySpringHttpBootstrap 
		extends EzyLoggable implements EzyHttpBootstrap {

	@Setter
	protected EzyServerContext serverContext;
	
	protected ConfigurableApplicationContext applicationContext;
	
	@Override
	public void start() throws Exception {
		SpringApplication app = newApplication();
		applicationContext = app.run();
	}
	
	@Override
	public void destroy() {
		SpringApplication.exit(applicationContext, () -> 0);
	}
	
	protected SpringApplication newApplication() {
		EzySpringApplication app = new EzySpringApplication(getClass());
		app.setServerContext(serverContext);
		return app;
	}
	
}

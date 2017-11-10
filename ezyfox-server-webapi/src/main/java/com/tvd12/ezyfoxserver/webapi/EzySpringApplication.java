package com.tvd12.ezyfoxserver.webapi;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tvd12.ezyfoxserver.context.EzyServerContext;

import lombok.Setter;

@Setter
public class EzySpringApplication extends SpringApplication {

	protected EzyServerContext serverContext;
	
	public EzySpringApplication(Object... sources) {
		super(sources);
	}
	
	@Override
	protected ConfigurableApplicationContext createApplicationContext() {
		ConfigurableApplicationContext ctx = super.createApplicationContext();
		ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
		beanFactory.registerSingleton("serverContext", serverContext);
		return ctx;
	}
	
}

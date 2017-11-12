package com.tvd12.ezyfoxserver.webapi;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.monitor.EzyMonitor;

import lombok.Setter;

@Setter
public class EzyWebApiApplication extends SpringApplication {

	protected EzyServerContext serverContext;
	
	public EzyWebApiApplication(Object... sources) {
		super(sources);
	}
	
	protected EzyMonitor newMonitor() {
		return new EzyMonitor();
	}
	
	@Override
	protected ConfigurableApplicationContext createApplicationContext() {
		ConfigurableApplicationContext ctx = super.createApplicationContext();
		ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
		beanFactory.registerSingleton("monitor", newMonitor());
		beanFactory.registerSingleton("serverContext", serverContext);
		return ctx;
	}
	
}

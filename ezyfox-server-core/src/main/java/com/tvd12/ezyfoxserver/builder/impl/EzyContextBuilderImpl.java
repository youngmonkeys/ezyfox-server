package com.tvd12.ezyfoxserver.builder.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.builder.EzyContextBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.config.EzyPlugin;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimpleContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;
import com.tvd12.ezyfoxserver.service.impl.EzyResponseSerializerImpl;

public class EzyContextBuilderImpl implements EzyContextBuilder<EzyContextBuilderImpl> {

	private EzyServer boss;
	
	public static EzyContextBuilderImpl builder() {
		return new EzyContextBuilderImpl();
	}
	
	@Override
	public EzyServerContext build() {
		EzySimpleContext context = new EzySimpleContext();
		context.setBoss(boss);
		context.setWorkerExecutor(newWorkerExecutor());
		context.addAppContexts(newAppContexts(context));
		context.addPluginContexts(newPluginContexts(context));
		context.setProperty(EzyResponseSerializer.class, newResponseSerializer());
		return context;
	}
	
	protected ExecutorService newWorkerExecutor() {
		String threadName = "worker";
		int nthreads = boss.getSettings().getNumThreads();
		return EzyExecutors.newFixedThreadPool(nthreads, threadName);
	}
	
	protected Collection<EzyAppContext> newAppContexts(EzyContext parent) {
		Collection<EzyAppContext> contexts = new ArrayList<>();
		for(Integer appId : boss.getAppIds())
			contexts.add(newAppContext(parent, boss.getAppById(appId)));
		return contexts;
	}
	
	protected EzyAppContext newAppContext(EzyContext parent, EzyApp app) {
		EzySimpleAppContext appContext = new EzySimpleAppContext();
		appContext.setApp(app);
		appContext.setParent(parent);
		appContext.setWorkerExecutor(newAppWorkerExecutor(app));
		return appContext;
	}
	
	protected Collection<EzyPluginContext> newPluginContexts(EzyContext parent) {
		Collection<EzyPluginContext> contexts = new ArrayList<>();
		for(Integer appId : boss.getPluginIds())
			contexts.add(newPluginContext(parent, boss.getPluginById(appId)));
		return contexts;
	}
	
	protected EzyPluginContext newPluginContext(EzyContext parent, EzyPlugin plugin) {
		EzySimplePluginContext pluginContext = new EzySimplePluginContext();
		pluginContext.setPlugin(plugin);
		pluginContext.setParent(parent);
		pluginContext.setWorkerExecutor(newPluginWorkerExecutor(plugin));
		return pluginContext;
	}
	
	protected ExecutorService newAppWorkerExecutor(EzyApp app) {
		String threadName = "app-worker";
		int nthreads = app.getNumThreads();
		return EzyExecutors.newFixedThreadPool(nthreads, threadName);
	}
	
	protected ExecutorService newPluginWorkerExecutor(EzyPlugin plugin) {
		String threadName = "plugin-worker";
		int nthreads = plugin.getNumThreads();
		return EzyExecutors.newFixedThreadPool(nthreads, threadName);
	}
	
	protected EzyResponseSerializer newResponseSerializer() {
		return EzyResponseSerializerImpl.builder().build();
	}
	
	@Override
	public EzyContextBuilderImpl boss(EzyServer boss) {
		this.boss = boss;
		return this;
	}

}

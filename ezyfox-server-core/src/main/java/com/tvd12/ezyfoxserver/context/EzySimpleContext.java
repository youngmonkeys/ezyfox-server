package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFetchAppByName;
import com.tvd12.ezyfoxserver.command.EzyFetchServer;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.command.impl.EzyFetchAppByNameImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyFetchServerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyRunWorkerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendMessageImpl;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleContext extends EzyBaseContext implements EzyServerContext {

	@Setter
	@Getter
	protected EzyServer boss;
	@Setter
	@Getter
	protected ExecutorService workerExecutor;
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> commandSuppliers;
	
	protected Map<Integer, EzyAppContext> appContexts;
	
	{
		appContexts = new HashMap<>();
		commandSuppliers = defaultCommandSuppliers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz).get();
		if(containsKey(clazz))
			return getProperty(clazz);
		throw new IllegalArgumentException("has no instance of " + clazz);
	}
	
	public void addAppContext(int appId, EzyAppContext appContext) {
		appContexts.putIfAbsent(appId, appContext);
	}
	
	public void addAppContexts(Collection<EzyAppContext> appContexts) {
		for(EzyAppContext ctx : appContexts)
			addAppContext(ctx.getApp().getId(), ctx);
	}
	
	@Override
	public EzyAppContext getAppContext(int appId) {
		if(appContexts.containsKey(appId))
			return appContexts.get(appId);
		throw new IllegalArgumentException("has not app with id = " + appId);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		answer.put(EzySendMessage.class, () -> new EzySendMessageImpl());
		answer.put(EzyRunWorker.class, () -> new EzyRunWorkerImpl(getWorkerExecutor()));
		answer.put(EzyFetchServer.class, () -> new EzyFetchServerImpl(getBoss()));
		answer.put(EzyFetchAppByName.class, () -> new EzyFetchAppByNameImpl(getBoss()));
		return answer;
	}
	
}

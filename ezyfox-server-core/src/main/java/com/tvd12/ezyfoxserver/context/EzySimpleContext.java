package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.command.impl.EzySendMessageImpl;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleContext extends EzyBaseContext implements EzyServerContext {

	@Setter
	@Getter
	protected EzyServer boss;
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> suppliers;
	
	protected Map<Integer, EzyAppContext> appContexts;
	
	{
		appContexts = new HashMap<>();
		suppliers = defaultCommandCreators();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(suppliers.containsKey(clazz))
			return (T) suppliers.get(clazz).get();
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
	
	@SuppressWarnings("rawtypes")
	public Map<Class, Supplier> defaultCommandCreators() {
		Map<Class, Supplier> answer = new HashMap<>();
		answer.put(EzySendMessage.class, EzySendMessageImpl::new);
		return answer;
	}

	@Override
	public EzyAppContext getAppContext(int appId) {
		if(appContexts.containsKey(appId))
			return appContexts.get(appId);
		throw new IllegalArgumentException("has not app with id = " + appId);
	}
	
}

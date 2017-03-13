package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyServer;

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
	public <T> T command(Class<T> clazz) {
		if(suppliers.containsKey(clazz))
			return (T) suppliers.get(clazz);
		throw new IllegalArgumentException("has no command with " + clazz);
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
		return answer;
	}
	
}

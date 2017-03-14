package com.tvd12.ezyfoxserver.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.command.impl.EzyRunWorkerImpl;
import com.tvd12.ezyfoxserver.config.EzyApp;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleAppContext extends EzyBaseContext implements EzyAppContext {

	@Setter
	@Getter
	protected EzyApp app;
	@Setter
	protected EzyContext parent;
	@Setter
	@Getter
	protected ExecutorService workerExecutor;
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> commandSuppliers;
	
	{
		commandSuppliers = defaultCommandSuppliers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz);
		if(containsKey(clazz))
			return getProperty(clazz);
		return parent.get(clazz);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new HashMap<>();
		answer.put(EzyRunWorker.class, () -> new EzyRunWorkerImpl(getWorkerExecutor()));
		return answer;
	}

}

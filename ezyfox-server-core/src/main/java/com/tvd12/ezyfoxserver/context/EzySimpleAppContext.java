package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
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
	protected Set<Class> unsafeCommands;
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> commandSuppliers;
	
	{
		unsafeCommands = defaultUnsafeCommands();
		commandSuppliers = defaultCommandSuppliers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(unsafeCommands.contains(clazz))
			throw newUnsafeCommandException(clazz);
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz);
		if(containsKey(clazz))
			return getProperty(clazz);
		return parent.get(clazz);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		answer.put(EzyRunWorker.class, () -> new EzyRunWorkerImpl(getWorkerExecutor()));
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected Set<Class> defaultUnsafeCommands() {
		Set<Class> answer = Sets.newConcurrentHashSet();
		answer.add(EzyFireEvent.class);
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected RuntimeException newUnsafeCommandException(Class command) {
		return new IllegalArgumentException("can not execute command " + command + " at app context");
	}

}

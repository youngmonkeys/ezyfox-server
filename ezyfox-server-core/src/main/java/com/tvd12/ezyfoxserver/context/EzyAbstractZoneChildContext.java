package com.tvd12.ezyfoxserver.context;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractZoneChildContext extends EzyAbstractContext {

	@Setter 
	@Getter
	protected EzyZoneContext parent;
	
	@Setter
    @Getter
    protected ScheduledExecutorService executorService;
	
	protected Set<Class> unsafeCommands = defaultUnsafeCommands();
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		if(unsafeCommands.contains(clazz))
			throw newUnsafeCommandException(clazz);
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz).get();
		if(containsKey(clazz))
			return getProperty(clazz);
		return parent.get(clazz);
	}
	
	protected Set<Class> defaultUnsafeCommands() {
		Set<Class> answer = Sets.newConcurrentHashSet();
		addUnsafeCommands(answer);
		return answer;
	}
	
	protected void addUnsafeCommands(Set<Class> unsafeCommands) {
	}
	
	protected RuntimeException newUnsafeCommandException(Class command) {
		return new IllegalArgumentException("can not execute command " + command + " at app context");
	}
}

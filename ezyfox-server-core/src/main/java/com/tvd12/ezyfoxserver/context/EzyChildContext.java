package com.tvd12.ezyfoxserver.context;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Setter;

public class EzyChildContext extends EzyAbstractContext {

	@Setter
	protected EzyContext parent;
	
	@SuppressWarnings("rawtypes")
	protected Set<Class> unsafeCommands;
	
	{
		unsafeCommands = defaultUnsafeCommands();
	}
	
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
	
	@SuppressWarnings("rawtypes")
	protected Set<Class> defaultUnsafeCommands() {
		Set<Class> answer = Sets.newConcurrentHashSet();
		addUnsafeCommands(answer);
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addUnsafeCommands(Set<Class> unsafeCommands) {
		//TODO add some commands
	}
	
	@SuppressWarnings("rawtypes")
	protected RuntimeException newUnsafeCommandException(Class command) {
		return new IllegalArgumentException("can not execute command " + command + " at app context");
	}
}

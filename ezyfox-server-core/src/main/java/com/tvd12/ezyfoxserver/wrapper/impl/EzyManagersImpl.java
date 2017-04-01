package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyManagersImpl extends EzyLoggable implements EzyManagers {

	private Map<Object, Object> managers;
	
	protected EzyManagersImpl(Builder builder) {
		this.managers = builder.newManagers();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyManagers#getManager(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getManager(Class<T> clazz) {
		return (T) managers.get(clazz);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyManagers#addManager(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> void addManager(Class<T> clazz, T instance) {
		managers.put(clazz, instance);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyManagers#startManagers()
	 */
	@Override
	public void startManagers() {
		managers.values().forEach(this::startManager);
	}
	
	protected void startManager(Object manager) {
		try {
			if(manager instanceof EzyStartable)
				((EzyStartable) manager).start();
		} catch (Exception e) {
			getLogger().error("can not start manager " + manager, e);
		}
	}
		
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		protected Map<Object, Object> newManagers() {
			Map<Object, Object> answer = new ConcurrentHashMap<>();
			answer.put(EzyUserManager.class, EzyUserManagerImpl.builder().build());
			return answer; 
		}
		
		public EzyManagers build() {
			return new EzyManagersImpl(this);
		}
	
		
	}

}

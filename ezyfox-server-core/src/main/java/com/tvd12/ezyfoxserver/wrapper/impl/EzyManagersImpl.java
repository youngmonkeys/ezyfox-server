package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyManagersImpl extends EzyLoggable implements EzyManagers {

	private Map<Object, Object> managers;
	
	protected EzyManagersImpl(Builder builder) {
		this.managers = builder.newManagers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getManager(Class<T> managerClass) {
		return (T) managers.get(managerClass);
	}
	
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
			answer.put(EzyUserManager.class, new EzyUserManagerImpl.Builder().build());
			answer.put(EzySessionManager.class, new EzySessionManagerImpl.Builder().build());
			return answer; 
		}
		
		public EzyManagers build() {
			return new EzyManagersImpl(this);
		}
	
		
	}

}

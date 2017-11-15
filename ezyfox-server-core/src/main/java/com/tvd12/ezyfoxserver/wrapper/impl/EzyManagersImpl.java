package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;

public class EzyManagersImpl extends EzyLoggable implements EzyManagers {

	private Map<Object, Object> managers;
	
	protected EzyManagersImpl(Builder builder) {
		this.managers = builder.newManagers();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyManagers#getManager(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getManager(Class<T> clazz) {
		return (T) managers.get(clazz);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyManagers#addManager(java.lang.Class, java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public void addManager(Class clazz, Object instance) {
		managers.put(clazz, instance);
	}
	
	@Override
	public void stopManagers() {
	    managers.values().forEach(this::stopManager);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyManagers#startManagers()
	 */
	@Override
	public void startManagers() {
		managers.values().forEach(this::startManager);
	}
	
	protected void stopManager(Object manager) {
        try {
            if(manager instanceof EzyDestroyable)
                ((EzyDestroyable) manager).destroy();
        } catch (Exception e) {
            getLogger().error("can not stop manager " + manager, e);
        }
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
			return answer; 
		}
		
		public EzyManagers build() {
			return new EzyManagersImpl(this);
		}
	
		
	}

}

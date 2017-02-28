package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyManagersImpl implements EzyManagers {

	private Map<Object, Object> managers;
	
	protected EzyManagersImpl(Builder builder) {
		this.managers = builder.newManagers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getManager(Class<T> managerClass) {
		return (T) managers.get(managerClass);
	}
		
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		protected Map<Object, Object> newManagers() {
			Map<Object, Object> answer = new HashMap<>();
			answer.put(EzyUserManager.class, new EzyUserManagerImpl.Builder().build());
			answer.put(EzySessionManager.class, new EzySessionManagerImpl.Builder().build());
			return answer; 
		}
		
		public EzyManagers build() {
			return new EzyManagersImpl(this);
		}
	
		
	}

}

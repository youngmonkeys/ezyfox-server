package com.tvd12.ezyfoxserver.hazelcast.bean;

import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.hazelcast.impl.EzySimpleSerivcesImplementor;

public interface EzyServicesImplementor {
	
	public static EzyServicesImplementor newInstance() {
		return new EzySimpleSerivcesImplementor();
	}
	
	public abstract EzyServicesImplementor scan(String packageName);
	
	public abstract EzyServicesImplementor scan(String... packageNames);
	
	public abstract EzyServicesImplementor scan(Iterable<String> packageNames);
	
	public abstract EzyServicesImplementor serviceInterface(String mapName, Class<?> itf);
	
	public abstract Map<Class<?>, Object> implement(HazelcastInstance hzInstance);
	
}

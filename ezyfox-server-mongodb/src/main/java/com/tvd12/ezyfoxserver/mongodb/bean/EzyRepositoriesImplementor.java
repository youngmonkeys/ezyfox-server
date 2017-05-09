package com.tvd12.ezyfoxserver.mongodb.bean;

import java.util.Map;

public interface EzyRepositoriesImplementor {
	
	public abstract EzyRepositoriesImplementor scan(String packageName);
	
	public abstract EzyRepositoriesImplementor scan(String... packageNames);
	
	public abstract EzyRepositoriesImplementor scan(Iterable<String> packageNames);
	
	public abstract EzyRepositoriesImplementor repositoryInterface(Class<?> itf);
	
	public abstract EzyRepositoriesImplementor repositoryInterfaces(Class<?>... itfs);
	
	public abstract EzyRepositoriesImplementor repositoryInterfaces(Iterable<Class<?>> itfs);
	
	public abstract Map<Class<?>, Object> implement(Object template);
	
}

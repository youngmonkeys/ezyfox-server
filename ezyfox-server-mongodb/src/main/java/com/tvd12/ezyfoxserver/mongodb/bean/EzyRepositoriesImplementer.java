package com.tvd12.ezyfoxserver.mongodb.bean;

import java.util.Map;

public interface EzyRepositoriesImplementer {
	
	public abstract EzyRepositoriesImplementer scan(String packageName);
	
	public abstract EzyRepositoriesImplementer scan(String... packageNames);
	
	public abstract EzyRepositoriesImplementer scan(Iterable<String> packageNames);
	
	public abstract EzyRepositoriesImplementer repositoryInterface(Class<?> itf);
	
	public abstract EzyRepositoriesImplementer repositoryInterfaces(Class<?>... itfs);
	
	public abstract EzyRepositoriesImplementer repositoryInterfaces(Iterable<Class<?>> itfs);
	
	public abstract Map<Class<?>, Object> implement(Object template);
	
}

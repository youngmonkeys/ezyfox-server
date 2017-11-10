package com.tvd12.ezyfoxserver.bean;

public interface EzyBeanFetcher {

	Object getBean(Class<?> type);
	
	Object getBean(String name, Class<?> type);
	
}

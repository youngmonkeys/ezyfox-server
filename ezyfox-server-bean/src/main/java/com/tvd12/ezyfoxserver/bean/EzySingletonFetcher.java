package com.tvd12.ezyfoxserver.bean;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface EzySingletonFetcher {

	<T> T getSingleton(String name, Class<T> type);
	
	<T> T getSingleton(Map properties);
	
	List getSingletons(Map properties);
	
}

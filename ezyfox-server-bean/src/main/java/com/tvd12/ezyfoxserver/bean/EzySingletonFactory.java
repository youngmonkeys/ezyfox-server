package com.tvd12.ezyfoxserver.bean;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface EzySingletonFactory {

	Object getSingleton(String name, Class type);
	
	Object getSingleton(Map properties);
	
	List getSingletons(Map properties);
	
	List getSingletons(Class annoClass);
	
	Map getProperties(Object singleton);
	
	Object addSingleton(Object singleton);
	
	Object addSingleton(String name, Object singleton);
	
	Object addSingleton(String name, Object singleton, Map properties);
	
}

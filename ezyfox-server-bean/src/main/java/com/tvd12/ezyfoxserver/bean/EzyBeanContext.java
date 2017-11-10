package com.tvd12.ezyfoxserver.bean;

import java.util.Properties;

import com.tvd12.ezyfoxserver.bean.impl.EzySimpleBeanContext;

public interface EzyBeanContext extends 
		EzyBeanFetcher, 
		EzySingletonFetcher, 
		EzyPrototypeFetcher,
		EzyPropertyFetcher {
	
	Properties getProperties();
	EzySingletonFactory getSingletonFactory();
	EzyPrototypeFactory getPrototypeFactory();
	EzyBeanNameTranslator getBeanNameTranslator();
	
	static EzyBeanContextBuilder builder() {
		return EzySimpleBeanContext.builder();
	}
	
}

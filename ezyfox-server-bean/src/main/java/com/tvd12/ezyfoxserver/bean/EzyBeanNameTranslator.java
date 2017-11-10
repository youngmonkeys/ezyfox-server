package com.tvd12.ezyfoxserver.bean;

public interface EzyBeanNameTranslator {

	String translate(String name, Class<?> type);
	
	void map(String freename, Class<?> type, String realname);
	
}

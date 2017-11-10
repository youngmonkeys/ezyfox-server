package com.tvd12.ezyfoxserver.bean;

public interface EzyPrototypeSupplier {

	Class<?> getObjectType();
	
	Object supply(EzyBeanContext context);
	
}

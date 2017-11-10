package com.tvd12.ezyfoxserver.reflect;

public interface EzyKnownTypeElement {

	@SuppressWarnings("rawtypes")
	Class getType();
	
	default String getTypeName() {
		return getType().getTypeName();
	}
	
}

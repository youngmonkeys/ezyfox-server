package com.tvd12.ezyfoxserver.binding.impl;

import java.util.List;

import com.tvd12.ezyfoxserver.reflect.EzyClass;

public interface EzyElementsFetcher {

	List<Object> getElements(EzyClass clazz, int accessType);
	
}

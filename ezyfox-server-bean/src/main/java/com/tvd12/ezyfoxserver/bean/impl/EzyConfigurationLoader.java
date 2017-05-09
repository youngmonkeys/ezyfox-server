package com.tvd12.ezyfoxserver.bean.impl;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;

public interface EzyConfigurationLoader {

	EzyConfigurationLoader clazz(Class<?> clazz);
	
	EzyConfigurationLoader context(EzyBeanContext context);
	
	void load();
}

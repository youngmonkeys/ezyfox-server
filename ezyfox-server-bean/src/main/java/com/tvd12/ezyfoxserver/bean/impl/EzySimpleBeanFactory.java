package com.tvd12.ezyfoxserver.bean.impl;

import com.tvd12.ezyfoxserver.bean.EzyBeanNameTranslator;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

public class EzySimpleBeanFactory extends EzyLoggable {

	@Setter
	protected EzyBeanNameTranslator beanNameTranslator;
	
	protected final String translateBeanName(String name, Class<?> type) {
		if(beanNameTranslator == null)
			return name;
		return beanNameTranslator.translate(name, type);
	}
	
	protected final void mapBeanName(String freename, Class<?> type, String realname) {
		if(beanNameTranslator != null)
			beanNameTranslator.map(freename, type, realname);
	}
	
	protected final String getDefaultBeanName(Class<?> type) {
		return EzyClasses.getVariableName(type);
	}
}

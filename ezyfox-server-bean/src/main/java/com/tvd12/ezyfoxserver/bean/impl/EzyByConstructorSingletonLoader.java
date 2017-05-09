package com.tvd12.ezyfoxserver.bean.impl;

import java.lang.reflect.Constructor;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

@SuppressWarnings("rawtypes")
public class EzyByConstructorSingletonLoader
		extends EzySimpleSingletonLoader
		implements EzySingletonLoader {

	protected final Constructor<?> constructor;
	
	protected EzyByConstructorSingletonLoader(EzyClass clazz) {
		super(clazz);
		this.constructor = getConstructor(clazz);
	}
	
	@Override
	protected String[] getConstructorArgumentNames() {
		return getConstructorArgumentNames(constructor);
	}
	
	@Override
	protected Class<?>[] getConstructorParameterTypes() {
		return constructor.getParameterTypes();
	}
	
	@Override
	protected Object newSingletonByConstructor(
			EzyBeanContext context, Class[] parameterTypes) throws Exception {
		if(parameterTypes.length == 0)
			return clazz.newInstance();
		return constructor.newInstance(getArguments(parameterTypes, context));
	}
	
}

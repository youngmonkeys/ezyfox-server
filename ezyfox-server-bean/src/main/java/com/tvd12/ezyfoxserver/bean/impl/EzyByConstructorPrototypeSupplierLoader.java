package com.tvd12.ezyfoxserver.bean.impl;

import java.lang.reflect.Constructor;

import com.tvd12.ezyfoxserver.reflect.EzyClass;

public class EzyByConstructorPrototypeSupplierLoader 
		extends EzySimplePrototypeSupplierLoader
		implements EzyPrototypeSupplierLoader {

	protected final Constructor<?> constructor;
	
	public EzyByConstructorPrototypeSupplierLoader(EzyClass clazz) {
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
	
}

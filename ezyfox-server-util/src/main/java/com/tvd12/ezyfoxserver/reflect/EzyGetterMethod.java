package com.tvd12.ezyfoxserver.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class EzyGetterMethod extends EzyByFieldMethod {

	public EzyGetterMethod(Method method) {
		this(new EzyMethod(method));
	}
	
	public EzyGetterMethod(EzyMethod method) {
		super(method.getMethod());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getType() {
		return getReturnType();
	}
	
	@Override
	public Type getGenericType() {
		return getGenericReturnType();
	}
	
	@Override
	public String getFieldName() {
		String name = method.getName();
		if(name.length() <= 3)
			return name;
		if(name.startsWith("get"))
			return super.getFieldName();
		return name;
	}
	
}

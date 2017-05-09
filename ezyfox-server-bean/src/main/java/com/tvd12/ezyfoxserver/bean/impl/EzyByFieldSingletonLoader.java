package com.tvd12.ezyfoxserver.bean.impl;

import java.util.Map;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;

@SuppressWarnings("rawtypes")
public class EzyByFieldSingletonLoader
		extends EzySimpleSingletonLoader
		implements EzySingletonLoader {

	protected final EzyField field;
	
	public EzyByFieldSingletonLoader(
			EzyField field, Object configurator, Map<Class<?>, EzyMethod> methodsByType) {
		super(new EzyClass(field.getType()), configurator, methodsByType);
		this.field = field;
	}
	
	@Override
	protected Map getAnnotationProperties() {
		return EzyKeyValueParser.getSingletonProperties(
				field.getAnnotation(EzySingleton.class));
	}
	
	@Override
	protected String getSingletonName() {
		return EzyBeanNameParser.getSingletonName(
				field.getAnnotation(EzySingleton.class), field.getName());
	}
	
	@Override
	protected Class<?>[] getConstructorParameterTypes() {
		return new Class[0];
	}
	
	@Override
	protected Object newSingletonByConstructor(
			EzyBeanContext context, Class[] parameterTypes) throws Exception {
		return field.get(configurator);
	}
	
	@Override
	protected String[] getConstructorArgumentNames() {
		return new String[0];
	} 
	
	@Override
	protected Class[] getConstructorParameterTypes(Class clazz) {
		return new Class[0];
	}
	
	@Override
	protected void detectCircularDependency(Class[] parameterTypes, StringBuilder log) {
	}
	
}

package com.tvd12.ezyfoxserver.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.reflections.ReflectionUtils;

import com.google.common.collect.Lists;

public final class EzyMethods {

	private EzyMethods() {
	}
	
	public static Object invoke(Method method, Object obj, Object... args) {
		try {
			return method.invoke(obj, args);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can not call method " + method.getName(), e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Method> getMethods(Class clazz) {
		return new ArrayList<>(ReflectionUtils.getAllMethods(clazz));
	}
	
	@SuppressWarnings("rawtypes")
	public static Method getMethod(
			Class clazz, String methodName, Class... parameterTypes) {
		return new EzyMethodFinder(clazz, methodName, parameterTypes).find();
	}
	
	@SuppressWarnings("rawtypes")
	public static Method getPublicMethod(
			Class clazz, String methodName, Class... parameterTypes) {
		return MethodUtils.getAccessibleMethod(clazz, methodName, parameterTypes);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Method> getAnnotatedMethods(
			Class clazz, Class<? extends Annotation> annClass) {
		return MethodUtils.getMethodsListWithAnnotation(clazz, annClass);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Method> getDeclaredMethods(Class clazz) {
		return Lists.newArrayList(clazz.getDeclaredMethods());
	}
}

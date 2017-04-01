package com.tvd12.ezyfoxserver.reflect;

public abstract class EzyClassUtil {

	private EzyClassUtil() {
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final String className) {
		try {
			return (T) getClass(className).newInstance();
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getClass(final String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}

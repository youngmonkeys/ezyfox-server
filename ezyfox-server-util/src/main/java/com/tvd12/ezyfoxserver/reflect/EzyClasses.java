package com.tvd12.ezyfoxserver.reflect;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.util.EzyReturner;

public final class EzyClasses {

	private static final String DOT = ".";
	
	private EzyClasses() {
	}
	
	@SuppressWarnings("rawtypes")
	public static String getSimpleName(Class clazz) {
		String simpleName = clazz.getSimpleName();
		if(!simpleName.isEmpty()) return simpleName;
		String fullName = clazz.getName();
		if(!fullName.contains(DOT)) return fullName;
		return fullName.substring(fullName.lastIndexOf(DOT) + 1);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getVariableName(Class clazz) {
		return getVariableName(clazz, "");
	}
	
	@SuppressWarnings("rawtypes")
	public static String getVariableName(Class clazz, String ignoredSuffix) {
		String name = getSimpleName(clazz);
		String vname = name.substring(0, 1).toLowerCase() + name.substring(1);
		if(ignoredSuffix.isEmpty() 
				|| !vname.endsWith(ignoredSuffix)
				|| vname.length() == ignoredSuffix.length())
			return vname;
		int endIndex = vname.indexOf(ignoredSuffix);
		return vname.substring(0, endIndex);
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> (T) getClass(className).newInstance());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className, ClassLoader classLoader) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> (T) getClass(className, classLoader).newInstance());
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getClass(String className) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> Class.forName(className));
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getClass(String className, ClassLoader classLoader) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> Class.forName(className, true, classLoader));
	}
	
	public static <T> T newInstance(Class<T> clazz) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> clazz.newInstance());
	}
	
	public static <T> T newInstance(Constructor<T> constructor, Object... arguments) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> constructor.newInstance(arguments));
	}
	
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... paramTypes) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> clazz.getDeclaredConstructor(paramTypes));
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperClasses(Class clazz) {
		return flatSuperClasses(clazz, false);
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperClasses(Class clazz, boolean includeObject) {
		Set<Class> classes = new HashSet<>();
		Class superClass = clazz.getSuperclass();
		while(superClass != null) {
			if(superClass.equals(Object.class) && !includeObject )
				break;
			classes.add(superClass);
			superClass = superClass.getSuperclass();
		}
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatInterfaces(Class clazz) {
		Set<Class> classes = new HashSet<>();
		Class[] interfaces = clazz.getInterfaces();
		classes.addAll(Sets.newHashSet(interfaces));
		for(Class itf : interfaces)
			classes.addAll(flatInterfaces(itf));
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperAndInterfaceClasses(Class clazz) {
		return flatSuperAndInterfaceClasses(clazz, false);
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperAndInterfaceClasses(Class clazz, boolean includeObject) {
		Set<Class> classes = new HashSet<>();
		Set<Class> interfaces = flatInterfaces(clazz);
		Set<Class> superClasses = flatSuperClasses(clazz, includeObject);
		classes.addAll(interfaces);
		for(Class superClass : superClasses) {
			Set<Class> superAndInterfaceClasses = flatSuperAndInterfaceClasses(superClass, includeObject);
			classes.add(superClass);
			classes.addAll(superAndInterfaceClasses);
		}
		return classes;
	}
	
}

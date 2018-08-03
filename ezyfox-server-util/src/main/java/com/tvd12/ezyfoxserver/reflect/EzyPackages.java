package com.tvd12.ezyfoxserver.reflect;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Function;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.Utils;

public final class EzyPackages {

	public static Set<Class<?>> getAnnotatedClasses(
			String packageName, Class<? extends Annotation> annClass) {
		return scanPackage(packageName, ref -> ref.getTypesAnnotatedWith(annClass));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Set<Class<?>> getExtendsClasses(String packageName, Class superClass) {
		return scanPackage(packageName, ref -> ref.getSubTypesOf(superClass));
	}
	
	private static Set<Class<?>> 
			scanPackage(String packageName, Function<Reflections, Set<Class<?>>> function) {
		Reflections.log = null;
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> classes = function.apply(reflections);
		Reflections.log = Utils.findLogger(Reflections.class);
		return classes;
	}
	
	public static Set<Class<?>> getAnnotatedClasses(
			Class<?> context, String packageName, Class<? extends Annotation> annClass) {
		return scanPackage(context.getClassLoader(), packageName, ref -> ref.getTypesAnnotatedWith(annClass));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Set<Class<?>> getExtendsClasses(
			Class<?> context, String packageName, Class superClass) {
		return scanPackage(context.getClassLoader(), packageName, ref -> ref.getSubTypesOf(superClass));
	}
	
	public static Set<Class<?>> getAnnotatedClasses(
			ClassLoader classLoader, String packageName, Class<? extends Annotation> annClass) {
		return scanPackage(classLoader, packageName, ref -> ref.getTypesAnnotatedWith(annClass));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Set<Class<?>> getExtendsClasses(
			ClassLoader classLoader, String packageName, Class superClass) {
		return scanPackage(classLoader, packageName, ref -> ref.getSubTypesOf(superClass));
	}
	
	private static Set<Class<?>> 
			scanPackage(ClassLoader classLoader, String packageName, Function<Reflections, Set<Class<?>>> function) {
		Reflections.log = null;
		Configuration configuration = ConfigurationBuilder
				.build(packageName)
				.addClassLoader(classLoader);
		Reflections reflections = new Reflections(configuration);
		Set<Class<?>> classes = function.apply(reflections);
		Reflections.log = Utils.findLogger(Reflections.class);
		return classes;
	}
	
}

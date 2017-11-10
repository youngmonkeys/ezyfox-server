package com.tvd12.ezyfoxserver.bean.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPropertyFetcher;
import com.tvd12.ezyfoxserver.bean.EzySingletonFactory;
import com.tvd12.ezyfoxserver.bean.exception.EzyNewSingletonException;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;

@SuppressWarnings("rawtypes")
public abstract class EzySimpleSingletonLoader
		extends EzySimpleObjectBuilder
		implements EzySingletonLoader {

	protected final Object configurator;
	protected final Map<Class<?>, EzyMethod> methodsByType;
	
	protected EzySimpleSingletonLoader(EzyClass clazz) {
		this(clazz, null, new HashMap<>());
	}
	
	protected EzySimpleSingletonLoader(
			EzyClass clazz, Object configurator, Map<Class<?>, EzyMethod> methodsByType) {
		super(clazz);
		this.configurator = configurator;
		this.methodsByType = methodsByType;
	}
	
	@Override
	public final Object load(EzyBeanContext context) {
		try {
			return process(context);
		}
		catch(EzyNewSingletonException e) {
			throw e;
		}
		catch(Exception e) {
			throw new IllegalStateException("can not create singleton of class " + clazz, e);
		}
	}
	
	protected Class<?> getSingletonClass() {
		return clazz.getClazz();
	}
	
	private Object process(EzyBeanContext context) throws Exception {
		EzySingletonFactory factory = context.getSingletonFactory();
		Class[] parameterTypes = getConstructorParameterTypes();
		StringBuilder log = new StringBuilder().append(getSingletonClass());
		detectCircularDependency(parameterTypes, log);
		String name = getSingletonName();
		Object singleton = getOrCreateSingleton(context, name, parameterTypes);
		Map properties = getAnnotationProperties();
		Object answer = factory.addSingleton(name, singleton, properties);
		setPropertiesToFields(singleton, context);
		setPropertiesToMethods(singleton, context);
		setValueToBindingFields(answer, context);
		setValueToBindingMethods(answer, context);
		callPostInit(answer);
		return answer;
	}
	
	private Object getOrCreateSingleton(
			EzyBeanContext context, 
			String name, Class[] parameterTypes) throws Exception {
		EzySingletonFactory factory = context.getSingletonFactory();
		Object singleton = factory.getSingleton(name, getSingletonClass());
		if(singleton == null) {
			singleton = newSingletonByConstructor(context, parameterTypes);
			getLogger().debug("add singleton with name {} of {}, object = {}", name, singleton.getClass(), singleton);
		}
		return singleton;
	}
	
	protected String getSingletonName() {
		return EzyBeanNameParser.getSingletonName(getSingletonClass());
	}
	
	protected Map getAnnotationProperties() {
		return EzyKeyValueParser.getSingletonProperties(getSingletonClass());
	}
	
	protected abstract Object newSingletonByConstructor(
			EzyBeanContext context, Class[] parameterTypes) throws Exception;
	
	private void setPropertiesToFields(Object singleton, EzyPropertyFetcher fetcher) {
		for(EzyField field : propertyFields)
			setValueToPropertyField(field, singleton, fetcher);
	}
	
	@SuppressWarnings("unchecked")
	private void setValueToPropertyField(EzyField field, Object singleton, EzyPropertyFetcher fetcher) {
		String propertyName = getPropertyName(field);
		if(fetcher.containsProperty(propertyName))
			field.set(singleton, fetcher.getProperty(propertyName, field.getType()));
	}
	
	private void setPropertiesToMethods(Object singleton, EzyPropertyFetcher fetcher) {
		for(EzySetterMethod method : propertyMethods)
			setValueToPropertyMethod(method, singleton, fetcher);
	}
	
	@SuppressWarnings("unchecked")
	private void setValueToPropertyMethod(EzySetterMethod method, Object singleton, EzyPropertyFetcher fetcher) {
		String propertyName = getPropertyName(method);
		if(fetcher.containsProperty(propertyName))
			method.invoke(singleton, fetcher.getProperty(propertyName, method.getType()));
	}
	
	private void setValueToBindingFields(Object singleton, EzyBeanContext context) {
		for(EzyField field : bindingFields)
			setValueToBindingField(field, singleton, context);
	}
	
	private void setValueToBindingField(EzyField field, Object singleton, EzyBeanContext context) {
		Object value = getOrCreateSingleton(
				field.getType(), getBeanName(field), context);
		field.set(singleton, value);
		getLogger().debug("{} set field: {} with value: {}", clazz, field.getName(), value);
	}
	
	private void setValueToBindingMethods(Object singleton, EzyBeanContext context) {
		for(EzySetterMethod method : bindingMethods)
			setValueToBindingMethod(method, singleton, context);
	}
	
	private void setValueToBindingMethod(
			EzySetterMethod method, Object singleton, EzyBeanContext context) {
		Object value = getOrCreateSingleton(
				method.getType(), getBeanName(method), context);
		method.invoke(singleton, value);
		getLogger().debug("{} invoke method: {} with value: {}", clazz, method.getName(), value);
	}
	
	private void callPostInit(Object singleton) {
		List<EzyMethod> methods = getPostInitMethods();
		methods.forEach(m -> m.invoke(singleton));
	}
	
	protected final Object[] getArguments(Class[] parameterTypes, EzyBeanContext context) {
		Object[] arguments = new Object[parameterTypes.length];
		String[] argumentNames = getConstructorArgumentNames(); 
		for(int i = 0 ; i < parameterTypes.length ; i++) {
			arguments[i] = getOrCreateSingleton(parameterTypes[i], argumentNames[i], context);
		}
		return arguments;
	}
	
	private Object getOrCreateSingleton(
			Class type, String beanName, EzyBeanContext context) {
		EzySingletonFactory factory = context.getSingletonFactory();
		Object singleton = factory.getSingleton(beanName, type);
		if(singleton == null)
			singleton = createNewSingleton(type, beanName, context);
		return singleton;
	}
	
	private Object createNewSingleton(
			Class paramType, String beanName, EzyBeanContext context) {
		EzyMethod method = methodsByType.remove(paramType);
		if(method != null) {
			getLogger().debug("add singleton of {} with method {}", paramType, method);
			return new EzyByMethodSingletonLoader(method, configurator, methodsByType).load(context);
		}
		if(isAbstractClass(paramType)) {
			throw new EzyNewSingletonException(getSingletonClass(), paramType, beanName);
		}
		return new EzyByConstructorSingletonLoader(new EzyClass(paramType)).load(context);
	}
	
	protected void detectCircularDependency(Class[] parameterTypes, StringBuilder log) {
		for(Class paramType : parameterTypes) {
			log.append(" => ").append(paramType);
			if(paramType.equals(clazz.getClazz())) {
				throw new IllegalStateException("circular dependency detected, " + log);
			}
			else {
				detectCircularDependency(getConstructorParameterTypes(paramType), log);
			}
		}
	}
	
	protected Class[] getConstructorParameterTypes(Class clazz) {
		try {
			Constructor constructor = getConstructor(new EzyClass(clazz));
			return constructor.getParameterTypes();
		}
		catch(Exception e) {
			return new Class[0];
		}
	}
	
}

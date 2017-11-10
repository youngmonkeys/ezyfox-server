package com.tvd12.ezyfoxserver.bean.impl;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.tvd12.ezyfoxserver.bean.EzyBeanConfig;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanNameTranslator;
import com.tvd12.ezyfoxserver.bean.EzyBeanNameTranslatorAware;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextAware;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactory;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactoryAware;
import com.tvd12.ezyfoxserver.bean.EzySingletonFactory;
import com.tvd12.ezyfoxserver.bean.EzySingletonFactoryAware;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyPropertiesAware;

import static com.tvd12.ezyfoxserver.bean.impl.EzyBeanNameParser.*;

public class EzySimpleConfigurationLoader
		extends EzyLoggable
		implements EzyConfigurationLoader {

	protected EzyClass clazz;
	protected Properties properties;
	protected EzyBeanContext context;
	protected EzyPrototypeFactory prototypeFactory;
	protected EzySingletonFactory singletonFactory;
	protected EzyBeanNameTranslator beanNameTranslator;
	protected Map<Class<?>, EzyMethod> singletonMethods;
	
	@Override
	public EzyConfigurationLoader clazz(Class<?> configClass) {
		this.clazz = new EzyClass(configClass);
		this.singletonMethods = mapSingletonTypeMethods();
		return this;
	}
	
	@Override
	public EzyConfigurationLoader context(EzyBeanContext context) {
		this.context = context;
		this.properties = context.getProperties();
		this.singletonFactory = context.getSingletonFactory();
		this.prototypeFactory = context.getPrototypeFactory();
		this.beanNameTranslator = context.getBeanNameTranslator();
		return this;
	}
	
	@Override
	public void load() {
		Object configurator = newConfigurator();
		addSingletonByFields(configurator);
		addSingletonByMethods(configurator);
		addPrototypeByFields(configurator);
		addPrototypeByMethods(configurator);
	}
	
	private Object newConfigurator() {
		Object object = new EzyByConstructorSingletonLoader(clazz)
				.load(context);
		if(object instanceof EzyBeanContextAware)
			((EzyBeanContextAware)object).setContext(context);
		if(object instanceof EzyPropertiesAware)
			((EzyPropertiesAware)object).setProperties(properties);
		if(object instanceof EzySingletonFactoryAware)
			((EzySingletonFactoryAware)object).setSingletonFactory(singletonFactory);
		if(object instanceof EzyPrototypeFactoryAware)
			((EzyPrototypeFactoryAware)object).setPrototypeFactory(prototypeFactory);
		if(object instanceof EzyBeanNameTranslatorAware)
			((EzyBeanNameTranslatorAware)object).setBeanNameTranslator(beanNameTranslator);
		if(object instanceof EzyBeanConfig)
			((EzyBeanConfig)object).config();
		return object;
	}
	
	private void addSingletonByFields(Object configurator) {
		getSingletonFields().forEach(f -> addSingletonByField(f, configurator));
	}
	
	private void addSingletonByField(EzyField field, Object configurator) {
		String beanName = getSingletonName(field);
		Object current = singletonFactory.getSingleton(beanName, field.getType());
		if(current == null)
			new EzyByFieldSingletonLoader(
					field, configurator, singletonMethods).load(context);
	}
	
	private void addSingletonByMethods(Object configurator) {
		Set<Class<?>> types = new HashSet<>(singletonMethods.keySet());
		for(Class<?> type : types) {
			EzyMethod method = singletonMethods.remove(type);
			if(method != null) {
				getLogger().debug("add singleton of {} with method {}", type, method);
				addSingletonByMethod(method, configurator);
			}
		}
	}
	
	private void addSingletonByMethod(EzyMethod method, Object configurator) {
		String beanName = getSingletonName(method);
		Object current = singletonFactory.getSingleton(beanName, method.getReturnType());
		if(current == null) {
			new EzyByMethodSingletonLoader(
					method, configurator, singletonMethods).load(context);
		}
	}
	
	//============ prototype =================
	private void addPrototypeByFields(Object configurator) {
		getPrototypeFields().forEach(f -> addPrototypeByField(f, configurator));
	}
	
	private void addPrototypeByField(EzyField field, Object configurator) {
		String beanName = getPrototypeName(field);
		Object current = prototypeFactory.getSupplier(beanName, field.getType());
		if(current == null)
			new EzyByFieldPrototypeSupplierLoader(field, configurator).load(prototypeFactory);
	}
	
	private void addPrototypeByMethods(Object configurator) {
		Map<Class<?>, EzyMethod> methods = mapPrototypeTypeMethods();
		Set<Class<?>> types = new HashSet<>(methods.keySet());
		for(Class<?> type : types)
			addPrototypeByMethod(methods.remove(type), configurator, methods);
	}
	
	private void addPrototypeByMethod(EzyMethod method, 
			Object configurator, Map<Class<?>, EzyMethod> methods) {
		String beanName = getPrototypeName(method);
		Object current = prototypeFactory.getSupplier(beanName, method.getReturnType());
		if(current == null) {
			new EzyByMethodPrototypeSupplierLoader(method, configurator).load(prototypeFactory);
		}
	}
	
	//============ get components ==========
	
	private List<EzyField> getSingletonFields() {
		return getBeanFields(EzySingleton.class);
	}
	
	private Map<Class<?>, EzyMethod> mapSingletonTypeMethods() {
		return mapBeanTypeMethods(EzySingleton.class);
	}
	
	private List<EzyField> getPrototypeFields() {
		return getBeanFields(EzyPrototype.class);
	}
	
	private Map<Class<?>, EzyMethod> mapPrototypeTypeMethods() {
		return mapBeanTypeMethods(EzyPrototype.class);
	}
	
	private List<EzyField> getBeanFields(Class<? extends Annotation> annClass) {
		return clazz.getPublicFields(f -> f.isAnnotated(annClass));
	}
	
	private Map<Class<?>, EzyMethod> mapBeanTypeMethods(Class<? extends Annotation> annClass) {
		List<EzyMethod> methods = clazz.getPublicMethods(m ->
				m.isAnnotated(annClass) &&
				m.getReturnType() != void.class
		);
		return EzyMaps.newHashMap(methods, m -> m.getReturnType());
	}
}

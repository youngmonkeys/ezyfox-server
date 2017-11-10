package com.tvd12.ezyfoxserver.bean.impl;

import static com.tvd12.ezyfoxserver.bean.impl.EzyBeanNameParser.getPrototypeName;
import static com.tvd12.ezyfoxserver.bean.impl.EzyBeanNameParser.getSingletonName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.bean.EzyBeanNameTranslator;
import com.tvd12.ezyfoxserver.bean.EzyErrorHandler;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactory;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfoxserver.bean.EzySingletonFactory;
import com.tvd12.ezyfoxserver.bean.annotation.EzyConfiguration;
import com.tvd12.ezyfoxserver.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPackagesScan;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.bean.exception.EzyNewSingletonException;
import com.tvd12.ezyfoxserver.bean.exception.EzySingletonException;
import com.tvd12.ezyfoxserver.bean.supplier.EzyArrayListSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyCollectionSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyConcurrentHashMapSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyCopyOnWriteArrayListSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyCopyOnWriteArraySetSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyHashMapSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyHashSetSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyLinkedListSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyListSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyMapSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyQueueSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzySetSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyStackSupplier;
import com.tvd12.ezyfoxserver.bean.supplier.EzyTreeMapSupplier;
import com.tvd12.ezyfoxserver.properties.EzyPropertiesReader;
import com.tvd12.ezyfoxserver.properties.EzySimplePropertiesReader;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyHashMapSet;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyMapSet;

import lombok.Getter;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzySimpleBeanContext
		extends EzyLoggable
		implements EzyBeanContext {

	@Getter
	protected Properties properties;
	@Getter
	protected EzySingletonFactory singletonFactory;
	@Getter
	protected EzyPrototypeFactory prototypeFactory;
	@Getter
	protected EzyBeanNameTranslator beanNameTranslator;
	
	protected EzyPropertiesReader propertiesReader;
	
	@Override
	public Object getBean(String name, Class type) {
		Object object = getSingleton(name, type);
		if(object != null) 
			return object;
		return getPrototype(name, type);
	}
	
	@Override
	public Object getBean(Class<?> type) {
		return getBean(EzyBeanNameParser.getBeanName(type), type);
	}
	
	@Override
	public <T> T getSingleton(String name, Class<T> type) {
		return (T) singletonFactory.getSingleton(name, type);
	}
	
	@Override
	public <T> T getSingleton(Map properties) {
		return (T) singletonFactory.getSingleton(properties);
	}
	
	@Override
	public List getSingletons(Map properties) {
		return singletonFactory.getSingletons(properties);
	}
	
	@Override
	public <T> T getPrototype(String name, Class<T> type) {
		EzyPrototypeSupplier supplier = prototypeFactory.getSupplier(name, type);
		if(supplier == null)
			throw new IllegalArgumentException("has no bean with name = " + name + ", and type " + type);
		return (T) supplier.supply(this);
	}
	
	@Override
	public <T> T getPrototype(Map properties) {
		EzyPrototypeSupplier supplier = getPrototypeSupplier(properties);
		return supplier != null ? (T)supplier.supply(this) : null;
	}
	
	@Override
	public List getPrototypes(Map properties) {
		List list = new ArrayList<>();
		List<EzyPrototypeSupplier> suppliers = getPrototypeSuppliers(properties);
		for(EzyPrototypeSupplier supplier : suppliers)
			list.add(supplier.supply(this));
		return list;
	}
	
	@Override
	public EzyPrototypeSupplier getPrototypeSupplier(Map properties) {
		return prototypeFactory.getSupplier(properties);
	}
	
	@Override
	public List<EzyPrototypeSupplier> getPrototypeSuppliers(Map properties) {
		return prototypeFactory.getSuppliers(properties);
	}
	
	@Override
	public boolean containsProperty(Object key) {
		return properties.containsKey(key);
	}
	
	@Override
	public <T> T getProperty(Object key, Class<T> outType) {
		return propertiesReader.get(properties, key, outType);
	}
	
	public static EzyBeanContextBuilder builder() {
		return new Builder();
	}

	public static class Builder extends EzyLoggable implements EzyBeanContextBuilder {
		protected Properties properties;
		protected Set<Class> singletonClasses;
		protected Set<Class> prototypeClasses;
		protected Set<Class> packagesScanClasses;
		protected Set<Class> configurationClasses;
		protected Set<Class> configurationBeforeClasses;
		protected EzyPropertiesReader propertiesReader;
		protected EzySingletonFactory singletonFactory;
		protected EzyPrototypeFactory prototypeFactory;
		protected EzyBeanNameTranslator beanNameTranslator;
		
		protected EzyErrorHandler errorHandler;
		protected EzyMapSet<EzyBeanKey, Class<?>> unloadedSingletons;
		
		public Builder() {
			this.properties = new Properties();
			this.singletonClasses = new HashSet<>();
			this.prototypeClasses = new HashSet<>();
			this.packagesScanClasses = new HashSet<>();
			this.configurationClasses = new HashSet<>();
			this.configurationBeforeClasses = new HashSet<>();
			this.errorHandler = new EzySimpleErrorHandler();
			this.unloadedSingletons = new EzyHashMapSet<>();
			this.beanNameTranslator = new EzySimpleBeanNameTranslator();
			this.singletonFactory = newBeanFactory(new EzySimpleSingletonFactory());
			this.prototypeFactory = newBeanFactory(new EzySimplePrototypeFactory());
		}
		
		protected <T extends EzySimpleBeanFactory> T newBeanFactory(T factory) {
			factory.setBeanNameTranslator(beanNameTranslator);
			return factory;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#scan(java.lang.String)
		 */
		@Override
		public EzyBeanContextBuilder scan(String packageName) {
			singletonClasses.addAll(getAnnotatedClasses(packageName, EzySingleton.class));
			prototypeClasses.addAll(getAnnotatedClasses(packageName, EzyPrototype.class));
			packagesScanClasses.addAll(getAnnotatedClasses(packageName, EzyPackagesScan.class));
			configurationClasses.addAll(getAnnotatedClasses(packageName, EzyConfiguration.class));
			configurationBeforeClasses.addAll(getAnnotatedClasses(packageName, EzyConfigurationBefore.class));
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#scan(java.lang.String)
		 */
		@Override
		public EzyBeanContextBuilder scan(String... packageNames) {
			return scan(Sets.newHashSet(packageNames));
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#scan(java.lang.Iterable)
		 */
		@Override
		public EzyBeanContextBuilder scan(Iterable<String> packageNames) {
			packageNames.forEach(this::scan);
			return this;	
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addSingleton(java.lang.String, java.lang.Object)
		 */
		@Override
		public EzyBeanContextBuilder addSingleton(String name, Object singleton) {
			singletonFactory.addSingleton(name, singleton);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addPrototypeSupplier(java.lang.String, com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier)
		 */
		@Override
		public EzyBeanContextBuilder addPrototypeSupplier(String objectName, EzyPrototypeSupplier supplier) {
			prototypeFactory.addSupplier(objectName, supplier);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addSingletonClass(java.lang.Class)
		 */
		@Override
		public EzyBeanContextBuilder addSingletonClass(Class clazz) {
			this.singletonClasses.add(clazz);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addSingletonClasses(java.lang.Class)
		 */
		@Override
		public EzyBeanContextBuilder addSingletonClasses(Class... classes) {
			return addSingletonClasses(Sets.newHashSet(classes));
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addSingletonClasses(java.lang.Iterable)
		 */
		@Override
		public EzyBeanContextBuilder addSingletonClasses(Iterable<Class> classes) {
			classes.forEach(this::addSingletonClass);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addPrototypeClass(java.lang.Class)
		 */
		@Override
		public EzyBeanContextBuilder addPrototypeClass(Class clazz) {
			this.prototypeClasses.add(clazz);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addPrototypeClasses(java.lang.Class)
		 */
		@Override
		public EzyBeanContextBuilder addPrototypeClasses(Class... classes) {
			return addPrototypeClasses(Sets.newHashSet(classes));
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#addPrototypeClasses(java.lang.Iterable)
		 */
		@Override
		public EzyBeanContextBuilder addPrototypeClasses(Iterable<Class> classes) {
			classes.forEach(this::addPrototypeClass);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder#errorHandler(com.tvd12.ezyfoxserver.bean.EzyErrorHandler)
		 */
		@Override
		public EzyBeanContextBuilder errorHandler(EzyErrorHandler handler) {
			this.errorHandler = handler;
			return this;
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder#addProperties(java.util.Map)
		 */
		@Override
		public EzyBeanContextBuilder addProperties(Map properties) {
			this.properties.putAll(properties);
			return this;
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder#addProperty(java.lang.String, java.lang.Object)
		 */
		public EzyBeanContextBuilder addProperty(String key, Object value) {
			this.properties.put(key, value);
			return this;
		}
		
		@Override
		public EzyBeanContextBuilder propertiesReader(EzyPropertiesReader propertiesReader) {
			this.propertiesReader = propertiesReader;
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.tvd12.ezyfoxserver.bean.impl.EzyBeanContextBuilder#build()
		 */
		@Override
		public EzySimpleBeanContext build() {
			EzySimpleBeanContext context = new EzySimpleBeanContext();
			context.properties = properties;
			context.prototypeFactory = prototypeFactory;
			context.singletonFactory = singletonFactory;
			context.beanNameTranslator = beanNameTranslator;
			context.propertiesReader = getPropertiesReader();
			addSingleton("beanContext", context);
			addSingleton("singletonFactory", singletonFactory);
			addSingleton("prototypeFactory", prototypeFactory);
			addSingleton("beanNameTranslator", beanNameTranslator);
			scanPackagesScanClasses();
			prototypeClasses.removeAll(singletonClasses);
			loadConfigurationBeforeClasses(context);
			addScannedSingletonsToFactory(context);
			addDefaultPrototypeSuppliers();
			addScannedPrototypeSuppliersToFactory();
			tryLoadUncompletedSingletonsAgain(context, false);
			loadConfigurationClasses(context);
			tryLoadUncompletedSingletonsAgain(context, true);
			return context;
		}
		
		private Set<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annClass) {
			return EzyPackages.getAnnotatedClasses(packageName, annClass);
		}
		
		private void addScannedSingletonsToFactory(EzyBeanContext context) {
			for(Class type : singletonClasses)
				createAndLoadSingleton(context, type);
		}
		
		private void addScannedPrototypeSuppliersToFactory() {
			for(Class type : prototypeClasses)
				createAndLoadPrototypeSupplier(type);
		}
		
		private Object createAndLoadSingleton(EzyBeanContext context, Class type) {
			return createAndLoadSingleton(context, type, false);
		}
		
		private Object createAndLoadSingleton(EzyBeanContext context, Class type, boolean reload) {
			String beanName = getSingletonName(type);
			Object current = singletonFactory.getSingleton(beanName, type);
			if(current != null && !reload) return current;
			try {
				return new EzyByConstructorSingletonLoader(new EzyClass(type)).load(context);
			}
			catch(EzyNewSingletonException e) {
				unloadedSingletons.addItems(e.getErrorKey(), e.getSingletonClass());
				return null;
			}
		}
		
		private void createAndLoadPrototypeSupplier(Class type) {
			String beanName = getPrototypeName(type);
			Object current = prototypeFactory.getSupplier(beanName, type);
			if(current == null)
				new EzyByConstructorPrototypeSupplierLoader(new EzyClass(type)).load(prototypeFactory);
		}
		
		private void scanPackagesScanClasses() {
			packagesScanClasses.forEach(this::scanPackagesScanClass);
		}
		
		private void scanPackagesScanClass(Class<?> clazz) {
			scan(clazz.getAnnotation(EzyPackagesScan.class).value());
		}
		
		private void loadConfigurationBeforeClasses(EzyBeanContext context) {
			List<Class> classes = EzyConfigurationBeforeClassSorter.sort(configurationBeforeClasses);
			for(Class clazz : classes)
				loadConfigurationClass(clazz, context);
		}
		
		private void loadConfigurationClasses(EzyBeanContext context) {
			List<Class> classes = EzyConfigurationClassSorter.sort(configurationClasses);
			for(Class clazz : classes)
				loadConfigurationClass(clazz, context);
		}
		
		private void loadConfigurationClass(Class<?> clazz, EzyBeanContext context) {
			new EzySimpleConfigurationLoader().context(context).clazz(clazz).load();
		}
		
		private void tryLoadUncompletedSingletonsAgain(EzyBeanContext context, boolean finish) {
			Set<EzyBeanKey> keySet = new HashSet<>(unloadedSingletons.keySet());
			for(EzyBeanKey key : keySet) {
				Set<Class<?>> uncompleted = unloadedSingletons.get(key);
				getLogger().debug("unload bean: {}, uncompleted: {}", key, uncompleted);
				getLogger().debug("try load bean {} again", key);
				loadUncompletedSingletons(context, key, uncompleted, finish);
			}
			if(finish) {
				while(!unloadedSingletons.isEmpty())
					tryLoadUncompletedSingletonsAgain(context, finish);
			}
		}
		
		private void loadUncompletedSingletons(
				EzyBeanContext context, EzyBeanKey key, Set<Class<?>> uncompleted, boolean finish) {
			Object singleton = getSingletonOfErrorBeanKey0(context, key, finish);
			if(singleton == null) return;
			getLogger().debug("found singleton {} with key {}", singleton, key);
			for(Class<?> clazz : uncompleted) {
				createAndLoadSingleton(context, clazz, true);
			}
		}
		
		private Object getSingletonOfErrorBeanKey0(EzyBeanContext context, EzyBeanKey key, boolean finish) {
			Object singleton = null;
			try {
				singleton = getSingletonOfErrorBeanKey(context, key);
			}
			catch(EzySingletonException e) {
				if(finish) {
					errorHandler.handle(e);
					unloadedSingletons.remove(key);
				}
			}
			if(finish && singleton != null) { 
				unloadedSingletons.remove(key);
			}
			return singleton;
		}
		
		private Object getSingletonOfErrorBeanKey(EzyBeanContext context, EzyBeanKey key) {
			Object singleton = singletonFactory.getSingleton(key.getName(), key.getType());
			if(singleton != null) return singleton;
			return loadSingletonOfBeanKey(context, key);
		}
		
		private Object loadSingletonOfBeanKey(EzyBeanContext context, EzyBeanKey key) {
			for(EzyBeanKey i : unloadedSingletons.keySet()) {
				Set<Class<?>> classes = unloadedSingletons.get(i);
				for(Class<?> implClass : classes) {
					if(key.getType().isAssignableFrom(implClass)) {
						return createAndLoadSingleton(context, implClass, true);
					}
				}
			}
			throw EzySingletonException.implementationNotFound(key, unloadedSingletons.get(key));
		}
		
		private EzyPropertiesReader getPropertiesReader() {
			return propertiesReader != null ? propertiesReader : new EzySimplePropertiesReader();
		}
		
		private void addDefaultPrototypeSuppliers() {
			prototypeFactory.addSupplier(new EzyArrayListSupplier());
			prototypeFactory.addSupplier(new EzyCollectionSupplier());
			prototypeFactory.addSupplier(new EzyConcurrentHashMapSupplier());
			prototypeFactory.addSupplier(new EzyCopyOnWriteArrayListSupplier());
			prototypeFactory.addSupplier(new EzyCopyOnWriteArraySetSupplier());
			prototypeFactory.addSupplier(new EzyHashMapSupplier());
			prototypeFactory.addSupplier(new EzyHashSetSupplier());
			prototypeFactory.addSupplier(new EzyLinkedListSupplier());
			prototypeFactory.addSupplier(new EzyListSupplier());
			prototypeFactory.addSupplier(new EzyMapSupplier());
			prototypeFactory.addSupplier(new EzyQueueSupplier());
			prototypeFactory.addSupplier(new EzySetSupplier());
			prototypeFactory.addSupplier(new EzyStackSupplier());
			prototypeFactory.addSupplier(new EzyTreeMapSupplier());
		}
		
	}
	
}

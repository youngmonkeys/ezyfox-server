package com.tvd12.ezyfoxserver.support.entry;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestInterceptor;
import com.tvd12.ezyfox.core.util.EzyEventHandlerAnnotations;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntry;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPluginPrototypeController;
import com.tvd12.ezyfoxserver.support.factory.EzyPluginResponseFactory;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EzySimplePluginEntry extends EzyAbstractPluginEntry {

	@Override
	public void config(EzyPluginContext context) {
		preConfig(context);
		EzyBeanContext beanContext = createBeanContext(context);
		addEventControllers(context, beanContext);
		setPluginRequestController(context, beanContext);
		postConfig(context);
		postConfig(context, beanContext);
	}
	
	protected void preConfig(EzyPluginContext ctx) {}
	protected void postConfig(EzyPluginContext ctx) {}
	protected void postConfig(EzyPluginContext ctx, EzyBeanContext beanContext) {}
	
	private void addEventControllers(EzyPluginContext context, EzyBeanContext beanContext) {
		EzySetup setup = context.get(EzySetup.class);
		List<Object> eventControllers = beanContext.getSingletons(EzyEventHandler.class);
		for(Object controller : eventControllers) {
			Class<?> handlerType = controller.getClass();
			EzyEventHandler annotation = handlerType.getAnnotation(EzyEventHandler.class);
			String eventName = EzyEventHandlerAnnotations.getEvent(annotation);
			setup.addEventController(EzyEventType.valueOf(eventName), (EzyEventController) controller);
			logger.info("add  event {} controller {}", eventName, controller);
		}
	}
	
	private void setPluginRequestController(EzyPluginContext pluginContext, EzyBeanContext beanContext) {
		EzyPluginSetup setup = pluginContext.get(EzyPluginSetup.class);
		EzyPluginRequestController controller = newUserRequestController(beanContext);
		setup.setRequestController(controller);
	}
	
	protected EzyPluginRequestController newUserRequestController(EzyBeanContext beanContext) {
		return EzyUserRequestPluginPrototypeController.builder()
				.beanContext(beanContext)
				.build();
	}

	private EzyBeanContext createBeanContext(EzyPluginContext context) {
    	EzyBindingContext bindingContext = createBindingContext();
    	EzyMarshaller marshaller = bindingContext.newMarshaller();
    	EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
    	EzyResponseFactory pluginResponseFactory = createPluginResponseFactory(context, marshaller);
    	ScheduledExecutorService executorService = context.get(ScheduledExecutorService.class);
    	EzyBeanContextBuilder beanContextBuilder = EzyBeanContext.builder()
    			.addSingleton("pluginContext", context)
    			.addSingleton("marshaller", marshaller)
    			.addSingleton("unmarshaller", unmarshaller)
    			.addSingleton("executorService", executorService)
    			.addSingleton("zoneContext", context.getParent())
    			.addSingleton("serverContext", context.getParent().getParent())
    			.addSingleton("pluginResponseFactory", pluginResponseFactory);
		Class[] singletonClasses = getSingletonClasses();
		beanContextBuilder.addSingletonClasses(singletonClasses);
		Class[] prototypeClasses = getPrototypeClasses();
		beanContextBuilder.addPrototypeClasses(prototypeClasses);
		String[] scanablePackages = getScanableBeanPackages();
		if(scanablePackages.length > 0) {
			EzyReflection reflection = new EzyReflectionProxy(Arrays.asList(scanablePackages));
			beanContextBuilder.addSingletonClasses(
					(Set)reflection.getAnnotatedClasses(EzyRequestController.class));
			beanContextBuilder.addSingletonClasses(
					(Set)reflection.getAnnotatedClasses(EzyExceptionHandler.class));
			beanContextBuilder.addSingletonClasses(
					(Set)reflection.getAnnotatedClasses(EzyRequestInterceptor.class));
			beanContextBuilder.addAllClasses(reflection);
		}
		setupBeanContext(context, beanContextBuilder);
		return beanContextBuilder.build();
    }
    
    protected EzyBindingContext createBindingContext() {
		EzyBindingContextBuilder builder = EzyBindingContext.builder();
		String[] scanablePackages = getScanableBindingPackages();
		if(scanablePackages.length > 0)
			builder.scan(scanablePackages);
		EzySimpleBindingContext answer = builder.build();
		return answer;
	}
    
    private EzyResponseFactory createPluginResponseFactory(
			EzyPluginContext pluginContext, EzyMarshaller marshaller) {
		EzyPluginResponseFactory factory = new EzyPluginResponseFactory();
		factory.setPluginContext(pluginContext);
		factory.setMarshaller(marshaller);
		return factory;
	}

    protected Class[] getSingletonClasses() {
		return new Class[0];
    }
    
    protected Class[] getPrototypeClasses() {
    	return new Class[0];
    }
    
    protected String[] getScanableBindingPackages() {
    		return new String[0];
    }
    
	protected abstract String[] getScanableBeanPackages();
	
	protected void setupBeanContext(EzyPluginContext context, EzyBeanContextBuilder builder) {}
    
    @Override
	public void start() throws Exception {}

	@Override
	public void destroy() {}
	
}

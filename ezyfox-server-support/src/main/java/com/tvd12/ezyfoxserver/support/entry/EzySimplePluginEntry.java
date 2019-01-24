package com.tvd12.ezyfoxserver.support.entry;

import java.util.List;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfox.core.annotation.EzyServerEventHandler;
import com.tvd12.ezyfox.core.util.EzyServerEventHandlerAnnotations;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntry;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPluginPrototypeController;
import com.tvd12.ezyfoxserver.support.factory.EzyPluginResponseFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EzySimplePluginEntry extends EzyAbstractPluginEntry {

	@Override
	public void config(EzyPluginContext context) {
		preConfig(context);
		EzyBeanContext beanContext = createBeanContext(context);
		addEventControllers(context, beanContext);
		setPluginRequestController(context, beanContext);
		postConfig(context);
	}
	
	protected void preConfig(EzyPluginContext ctx) {}
	protected void postConfig(EzyPluginContext ctx) {}
	
	private void addEventControllers(EzyPluginContext context, EzyBeanContext beanContext) {
		EzySetup setup = context.get(EzySetup.class);
		List<Object> eventControllers = beanContext.getSingletons(EzyServerEventHandler.class);
		for(Object controller : eventControllers) {
			Class<?> handlerType = controller.getClass();
			EzyServerEventHandler annotation = handlerType.getAnnotation(EzyServerEventHandler.class);
			String eventName = EzyServerEventHandlerAnnotations.getEvent(annotation);
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
    	EzyBeanContextBuilder beanContextBuilder = EzyBeanContext.builder()
    			.addSingleton("pluginContext", context)
    			.addSingleton("marshaller", marshaller)
    			.addSingleton("unmarshaller", unmarshaller)
    			.addSingleton("zoneContext", context.getParent())
			.addSingleton("serverContext", context.getParent().getParent());
    		Class[] defaultSingletonClasses = getDefaultSingletonClasses();
    		beanContextBuilder.addSingletonClasses(defaultSingletonClasses);
    		Class[] singletonClasses = getSingletonClasses();
    		beanContextBuilder.addSingletonClasses(singletonClasses);
		String[] scanablePackages = getScanableBeanPackages();
		beanContextBuilder.scan(scanablePackages);
		setupBeanContext(context, beanContextBuilder);
		return beanContextBuilder.build();
    }
    
    protected EzyBindingContext createBindingContext() {
		EzyBindingContextBuilder builder = EzyBindingContext.builder();
		String[] scanablePackages = getScanableBindingPackages();
		builder.scan(scanablePackages);
		EzySimpleBindingContext answer = builder.build();
		return answer;
	}
    
    protected Class[] getDefaultSingletonClasses() {
    		return new Class[] {
    				EzyPluginResponseFactory.class
    		};
    }

    protected Class[] getSingletonClasses() {
		return new Class[0];
    }
    
    protected String[] getScanableBindingPackages() {
    		return new String[0];
    }
    
	protected abstract String[] getScanableBeanPackages();
	protected abstract void setupBeanContext(EzyPluginContext context, EzyBeanContextBuilder builder);
    
    @Override
	public void start() throws Exception {}

	@Override
	public void destroy() {}
	
}

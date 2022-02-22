package com.tvd12.ezyfoxserver.support.controller;

import static com.tvd12.ezyfox.io.EzyStrings.isNotBlank;
import static com.tvd12.ezyfoxserver.support.constant.EzySupportConstants.PROPERTY_NAME_MODULE_ALLOW_OVERRIDE_URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestInterceptor;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.core.util.EzyRequestInterceptorAnnotations;
import com.tvd12.ezyfox.core.util.EzyRequestListenerAnnotations;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.reflect.EzyClassTree;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlersImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlersImplementer;
import com.tvd12.ezyfoxserver.support.exception.EzyUserRequestException;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandlerProxy;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestInterceptor;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EzyUserRequestSingletonController<
		C extends EzyZoneChildContext, 
		E extends EzyUserRequestEvent>
		extends EzyAbstractUserRequestController{

	protected final EzyUnmarshaller unmarshaller;
	protected final EzyResponseFactory responseFactory;
	protected final List<Class<?>> handledExceptionClasses;
	protected final Map<String, EzyUserRequestHandler> requestHandlers;
	protected final List<EzyUserRequestInterceptor> requestInterceptors;
	protected final Map<Class<?>, EzyUncaughtExceptionHandler> exceptionHandlers;
	protected final EzyUserRequestPrototypeController prototypeController;
	
	protected EzyUserRequestSingletonController(Builder<?> builder) {
		this.unmarshaller = builder.unmarshaller;
		this.responseFactory = builder.responseFactory;
		this.prototypeController = builder.getPrototypeController();
		this.requestHandlers = new HashMap<>(builder.extractRequestHandlers());
		this.exceptionHandlers = new HashMap<>(builder.getExceptionHandlers());
		this.requestInterceptors = new ArrayList<>(builder.getRequestInterceptors());
		this.handledExceptionClasses = new EzyClassTree(exceptionHandlers.keySet()).toList();
	}
	
	public void handle(C context, E event) {
		EzyArray data = event.getData();
		String cmd = data.get(0, String.class);
		EzyUserRequestHandler handler = requestHandlers.get(cmd);
		if(handler == null) {
			prototypeController.handle(context, event);
			return;
		}
		Object handlerData = data.get(1, EzyData.class, null);
		Class requestDataType = handler.getDataType();
		if(requestDataType != null) {
			handlerData = unmarshaller.unmarshal(handlerData, requestDataType);
		}
		try {
			preHandle(context, event, cmd, handlerData);
			handler.handle(context, event, handlerData);
			postHandle(context, event, cmd, handlerData);
		}
		catch (Exception e) {
			postHandle(context, event, cmd, handlerData, e);
			if(e instanceof EzyBadRequestException) {
				EzyBadRequestException ex = (EzyBadRequestException)e;
				if(ex.isSendToClient()) {
					EzyData errorData = newErrorData(ex);
					responseError(context, event, errorData);
				}
				logger.debug("request cmd: {} by session: {} with data: {} error", cmd, event.getSession().getName(), data, e);
			}
			else {
				EzyUncaughtExceptionHandler exceptionHandler = getExceptionHandler(e.getClass());
				if(exceptionHandler == null)
					throw new EzyUserRequestException(cmd, handlerData, e);
				try {
					exceptionHandler.handleException(context, event, cmd, handlerData, e);
				}
				catch (Exception ex) {
					throw new EzyUserRequestException(cmd, handlerData, ex);
				}
			}
		}
	}
	
	protected void preHandle(C context, E event, String cmd, Object data) {
		for(EzyUserRequestInterceptor interceptor : requestInterceptors)
			interceptor.preHandle(context, event, cmd, data);
	}
	
	protected void postHandle(C context, E event, String cmd, Object data) {
		for(EzyUserRequestInterceptor interceptor : requestInterceptors)
			interceptor.postHandle(context, event, cmd, data);
	}
	
	protected void postHandle(C context, E event, String cmd, Object data, Exception e) {
		for(EzyUserRequestInterceptor interceptor : requestInterceptors)
			interceptor.postHandle(context, event, cmd, data, e);
	}
	
	protected EzyUncaughtExceptionHandler getExceptionHandler(Class<?> exceptionClass) {
		for(Class<?> exc : handledExceptionClasses) {
			if(exc.isAssignableFrom(exceptionClass))
				return exceptionHandlers.get(exc);
		}
		return null;
	}
	
	protected abstract void responseError(C context, E event, EzyData errorData);
	
	@Override
	public Set<String> getCommands() {
	    Set<String> commands = new HashSet<>();
	    commands.addAll(requestHandlers.keySet());
	    commands.addAll(prototypeController.getCommands());
	    return commands;
	}
	
	public abstract static class Builder<B extends Builder>
			extends EzyLoggable
			implements EzyBuilder<EzyUserRequestSingletonController> {

		protected EzyBeanContext beanContext;
		protected EzyUnmarshaller unmarshaller;
		protected EzyResponseFactory responseFactory;
		protected EzySingletonFactory singletonFactory;
		protected EzyFeatureCommandManager featureCommandManager; 
		protected EzyRequestCommandManager requestCommandManager;
		
		public B beanContext(EzyBeanContext beanContext) {
			this.beanContext = beanContext;
			this.singletonFactory = beanContext.getSingletonFactory();
			this.responseFactory = beanContext.getSingleton(EzyResponseFactory.class);
			this.unmarshaller = beanContext.getSingleton("unmarshaller", EzyUnmarshaller.class);
			this.featureCommandManager = beanContext.getSingleton(EzyFeatureCommandManager.class);
            this.requestCommandManager = beanContext.getSingleton(EzyRequestCommandManager.class);
			return (B)this;
		}
		
		protected abstract EzyUserRequestPrototypeController getPrototypeController();
		
		private Map<String, EzyUserRequestHandler> extractRequestHandlers() {
			Map<String, EzyUserRequestHandlerProxy> fromListeners =
			    extractRequestHandlersFromListener();
			extractRequestCommands(fromListeners);
			Map<String, EzyUserRequestHandler> handlers = new HashMap<>(fromListeners);			
			handlers.putAll(implementClientRequestHandlers());
			return handlers;
		}
		
		private Map<String, EzyUserRequestHandlerProxy> extractRequestHandlersFromListener() {
		    List<Object> clientRequestHandlers = getClientRequestHandlers();
            Map<String, EzyUserRequestHandlerProxy> handlers = new HashMap<>();
            for(Object handler : clientRequestHandlers) {
                Class<?> handleType = handler.getClass();
                EzyRequestListener annotation = handleType.getAnnotation(EzyRequestListener.class);
                String command = EzyRequestListenerAnnotations.getCommand(annotation);
                handlers.put(command, new EzyUserRequestHandlerProxy((EzyUserRequestHandler) handler));
                logger.debug("add command {} and request handler {}", command, handler);
            }
            return handlers;
		}
		
		private void extractRequestCommands(Map<String, EzyUserRequestHandlerProxy> handlers) {
            for(String command : handlers.keySet()) {
                EzyUserRequestHandlerProxy handler = handlers.get(command);
                Class<?> handleType = handler.getHandler().getClass();
                this.requestCommandManager.addCommand(command);
                if (handleType.isAnnotationPresent(EzyManagement.class)) {
                    this.requestCommandManager.addManagementCommand(command);
                }
                if (handleType.isAnnotationPresent(EzyPayment.class)) {
                    this.requestCommandManager.addPaymentCommand(command);
                }
                EzyFeature featureAnno = handleType.getAnnotation(EzyFeature.class);
                if (featureAnno != null && isNotBlank(featureAnno.value())) {
                    this.featureCommandManager.addFeatureCommand(featureAnno.value(), command);
                }
            }
        }
		
		private List<EzyUserRequestInterceptor> getRequestInterceptors() {
			List<EzyUserRequestInterceptor> interceptors =
					singletonFactory.getSingletons(EzyRequestInterceptor.class);
			List<EzyUserRequestInterceptor> answer = new ArrayList<>();
			for(EzyUserRequestInterceptor interceptor : interceptors) {
				logger.debug("add interceptor {}", interceptor);
				answer.add(interceptor);
			}
			answer.sort((a, b) -> 
				EzyRequestInterceptorAnnotations.getPriority(a.getClass()) 
					- EzyRequestInterceptorAnnotations.getPriority(b.getClass())
			);
			return interceptors;
		}
		
		private Map<Class<?>, EzyUncaughtExceptionHandler> getExceptionHandlers() {
			Map<Class<?>, EzyUncaughtExceptionHandler> handlers = new HashMap<>();
			Map<Class<?>, EzyUncaughtExceptionHandler> implementedHandlers = implementExceptionHandlers();
			for(Class<?> exceptionClass : implementedHandlers.keySet()) {
				EzyUncaughtExceptionHandler handler = implementedHandlers.get(exceptionClass);
				logger.debug("add exception {} and handler {}", exceptionClass.getName(), handler);
				handlers.put(exceptionClass, handler);
			}
			return handlers;
		}
		
		private List<Object> getClientRequestHandlers() {
			return singletonFactory.getSingletons(EzyRequestListener.class);
		}
		
		private Map<String, EzyUserRequestHandler> implementClientRequestHandlers() {
			EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
			implementer.setResponseFactory(responseFactory);
			implementer.setFeatureCommandManager(featureCommandManager);
			implementer.setRequestCommandManager(requestCommandManager);
			implementer.setAllowOverrideCommand(
			    beanContext.getProperty(PROPERTY_NAME_MODULE_ALLOW_OVERRIDE_URI, boolean.class, false)
		    );
			Map<String, EzyUserRequestHandler> implementedHandlers =
			    implementer.implement(singletonFactory.getSingletons(EzyRequestController.class));
			for(String command : implementedHandlers.keySet()) {
                EzyUserRequestHandler handler = implementedHandlers.get(command);
                logger.debug("add command {} and request handler {}", command, handler);
            }
			return implementedHandlers;
		}
		
		private Map<Class<?>, EzyUncaughtExceptionHandler> implementExceptionHandlers() {
			EzyExceptionHandlersImplementer implementer = new EzyExceptionHandlersImplementer();
			return implementer.implement(singletonFactory.getSingletons(EzyExceptionHandler.class));
		}
	}
}

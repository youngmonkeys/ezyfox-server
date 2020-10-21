package com.tvd12.ezyfoxserver.support.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestController;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestInterceptor;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.core.util.EzyClientRequestListenerAnnotations;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.reflect.EzyClassTree;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlersImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlersImplementer;
import com.tvd12.ezyfoxserver.support.exception.EzyUserRequestException;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandlerProxy;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestInterceptor;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EzyUserRequestSingletonController<
		C extends EzyZoneChildContext, 
		E extends EzyUserRequestEvent>
		extends EzyAbstractUserRequestController{

	protected final EzyUnmarshaller unmarshaller;
	protected final List<Class<?>> handledExceptionClasses;
	protected final Map<String, EzyUserRequestHandler> requestHandlers;
	protected final List<EzyUserRequestInterceptor> requestInterceptors;
	protected final Map<Class<?>, EzyUncaughtExceptionHandler> exceptionHandlers;
	
	protected EzyUserRequestSingletonController(Builder<?> builder) {
		this.unmarshaller = builder.unmarshaller;
		this.requestHandlers = new HashMap<>(builder.getRequestHandlers());
		this.exceptionHandlers = new HashMap<>(builder.getExceptionHandlers());
		this.requestInterceptors = new ArrayList<>(builder.getRequestInterceptors());
		this.handledExceptionClasses = new EzyClassTree(exceptionHandlers.keySet()).toList();
	}
	
	public void handle(C context, E event) {
		EzyArray data = event.getData();
		String cmd = data.get(0, String.class);
		EzyData params = data.get(1, EzyData.class, null);
		EzyUserRequestHandler handler = requestHandlers.get(cmd);
		if(handler == null) {
			logger.warn("has no handler with command: {} from session: {}", cmd, event.getSession().getName());
			return;
		}
		Object handlerData = params;
		Class requestDataType = handler.getDataType();
		if(requestDataType != null) {
			handlerData = unmarshaller.unmarshal(handlerData, requestDataType);
		}
		try {
			preHandle(context, event, cmd, handlerData);
			handler.handle(context, event, handlerData);
			postHandle(context, event, cmd, handlerData);
		}
		catch(EzyBadRequestException e) {
			postHandle(context, event, cmd, handlerData, e);
			if(e.isSendToClient()) {
				EzyData errorData = newErrorData(e);
				responseError(context, event, errorData);
			}
			logger.debug("request cmd: {} by session: {} with data: {} error", cmd, event.getSession().getName(), data, e);
		}
		catch(Exception e) {
			postHandle(context, event, cmd, handlerData, e);
			try {
				if(!handleException(context, event, requestDataType, e))
					throw e;
			}
			catch (Exception ex) {
				throw new EzyUserRequestException(cmd, handlerData, ex);
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
	
	protected boolean handleException(C context, E event, Object data, Exception e) throws Exception {
		for(Class<?> exceptionClass : handledExceptionClasses) {
			if(exceptionClass.isAssignableFrom(e.getClass())) {
				EzyUncaughtExceptionHandler exceptionHandler = exceptionHandlers.get(exceptionClass);
				exceptionHandler.handleException(context, event, data, e);
				return true;
			}
		}
		return false;
	}
	
	protected abstract void responseError(C context, E event, EzyData errorData);
	
	public abstract static class Builder<B extends Builder>
			extends EzyLoggable
			implements EzyBuilder<EzyUserRequestSingletonController> {
		
		private EzySingletonFactory singletonFactory;
		private EzyUnmarshaller unmarshaller;
		
		public B beanContext(EzyBeanContext beanContext) {
			this.singletonFactory = beanContext.getSingletonFactory();
			this.unmarshaller = beanContext.getSingleton("unmarshaller", EzyUnmarshaller.class);
			return (B)this;
		}
		
		private Map<String, EzyUserRequestHandler> getRequestHandlers() {
			List<Object> clientRequestHandlers = getClientRequestHandlers();
			Map<String, EzyUserRequestHandler> handlers = new HashMap<>();
			for(Object handler : clientRequestHandlers) {
				Class<?> handleType = handler.getClass();
				EzyClientRequestListener annotation = handleType.getAnnotation(EzyClientRequestListener.class);
				String command = EzyClientRequestListenerAnnotations.getCommand(annotation);
				handlers.put(command, new EzyUserRequestHandlerProxy((EzyUserRequestHandler) handler));
				logger.debug("add command {} and request handler {}", command, handler);
			}
			Map<String, EzyUserRequestHandler> implementedHandlers = implementClientRequestHandlers();
			for(String command : implementedHandlers.keySet()) {
				EzyUserRequestHandler handler = implementedHandlers.get(command);
				logger.debug("add command {} and request handler {}", command, handler);
				handlers.put(command, handler);
			}
			return handlers;
		}
		
		private List<EzyUserRequestInterceptor> getRequestInterceptors() {
			List<EzyUserRequestInterceptor> interceptors =
					singletonFactory.getSingletons(EzyClientRequestInterceptor.class);
			for(EzyUserRequestInterceptor interceptor : interceptors)
				logger.debug("add interceptor {}", interceptor);
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
			return singletonFactory.getSingletons(EzyClientRequestListener.class);
		}
		
		private Map<String, EzyUserRequestHandler> implementClientRequestHandlers() {
			EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
			return implementer.implement(singletonFactory.getSingletons(EzyClientRequestController.class));
		}
		
		private Map<Class<?>, EzyUncaughtExceptionHandler> implementExceptionHandlers() {
			EzyExceptionHandlersImplementer implementer = new EzyExceptionHandlersImplementer();
			return implementer.implement(singletonFactory.getSingletons(EzyExceptionHandler.class));
		}
	}
}

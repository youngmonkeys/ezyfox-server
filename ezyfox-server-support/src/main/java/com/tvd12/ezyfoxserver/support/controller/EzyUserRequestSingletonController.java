package com.tvd12.ezyfoxserver.support.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.core.util.EzyClientRequestListenerAnnotations;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EzyUserRequestSingletonController<
		C extends EzyZoneChildContext, 
		E extends EzyUserRequestEvent>
		extends EzyAbstractUserRequestController{

	private final EzyUnmarshaller unmarshaller;
	private final Map<String, EzyUserRequestHandler> handlers;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected EzyUserRequestSingletonController(Builder<?> builder) {
		this.unmarshaller = builder.unmarshaller;
		this.handlers = builder.getHandlers();
	}
	
	public void handle(C context, E event) {
		EzyArray data = event.getData();
		String cmd = data.get(0, String.class);
		EzyData params = data.get(1, EzyData.class, null);
		EzyUserRequestHandler handler = handlers.get(cmd);
		if(handler == null) {
			logger.warn("has no handler with command: {} from session: {}", cmd, event.getSession().getName());
			return;
		}
		Object handlerData = handler.newData();
		if(params != null)
			unmarshaller.unwrap(params, handlerData);
		try {
			handler.handle(context, event.getUser(), handlerData);
		}
		catch(EzyBadRequestException e) {
			if(e.isSendToClient()) {
				EzyData errorData = newErrorData(e);
				responseError(context, event, errorData);
			}
			logger.debug("request cmd: " + cmd + " by session: " + event.getSession().getName() + " with data: " + data + " error", e);
		}
		catch(Exception e) {
			throw e;
		}
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
		
		private Map<String, EzyUserRequestHandler> getHandlers() {
			List<Object> singletons = filterSingletons();
			Map<String, EzyUserRequestHandler> handlers = new ConcurrentHashMap<>();
			for(Object singleton : singletons) {
				Class<?> handleType = singleton.getClass();
				EzyClientRequestListener annotation = handleType.getAnnotation(EzyClientRequestListener.class);
				String command = EzyClientRequestListenerAnnotations.getCommand(annotation);
				handlers.put(command, (EzyUserRequestHandler) singleton);
				logger.debug("add command {} and request handler singleton {}", command, singleton);
			}
			return handlers;
		}
		
		private List<Object> filterSingletons() {
			return singletonFactory.getSingletons(EzyClientRequestListener.class);
		}
	}
}

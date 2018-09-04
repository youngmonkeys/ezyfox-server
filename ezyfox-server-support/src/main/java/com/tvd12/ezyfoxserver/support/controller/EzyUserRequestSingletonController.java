package com.tvd12.ezyfoxserver.support.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EzyUserRequestSingletonController<C extends EzyContext, E extends EzyUserRequestEvent> {

	private final EzyUnmarshaller unmarshaller;
	private final Map<String, EzyUserRequestHandler> handlers;
	
	protected EzyUserRequestSingletonController(Builder<?> builder) {
		this.unmarshaller = builder.unmarshaller;
		this.handlers = builder.getHandlers();
	}
	
	public void handle(C context, E event) {
		EzyArray data = event.getData();
		String cmd = data.get(0, String.class);
		EzyData params = data.get(1, EzyData.class, null);
		EzyUserRequestHandler handler = handlers.get(cmd);
		Object handlerData = handler.newData();
		if(params != null)
			unmarshaller.unwrap(params, handlerData);
		handler.handle(context, event.getUser(), handlerData);
	}
	
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
				String command = annotation.command();
				handlers.put(command, (EzyUserRequestHandler) singleton);
				getLogger().debug("add command {} and request handler singleton {}", command, singleton);
			}
			return handlers;
		}
		
		private List<Object> filterSingletons() {
			return singletonFactory.getSingletons(EzyClientRequestListener.class);
		}
	}
}

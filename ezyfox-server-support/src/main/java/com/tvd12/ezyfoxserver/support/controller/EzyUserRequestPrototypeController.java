package com.tvd12.ezyfoxserver.support.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPrototypeFactory;
import com.tvd12.ezyfox.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.core.util.EzyClientRequestListenerAnnotations;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;
import com.tvd12.ezyfoxserver.entity.EzyUserAware;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;

public abstract class EzyUserRequestPrototypeController<
		C extends EzyZoneChildContext, 
		E extends EzyUserRequestEvent>
		extends EzyAbstractUserRequestController {

	protected final EzyBeanContext beanContext;
	protected final EzyUnmarshaller unmarshaller;
	protected final Map<String, EzyPrototypeSupplier> handlers;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected EzyUserRequestPrototypeController(Builder<?> builder) {
		this.beanContext = builder.beanContext;
		this.unmarshaller = builder.unmarshaller;
		this.handlers = builder.getHandlers();
	}
	
	public void handle(C context, E event) {
		EzyArray data = event.getData();
		String cmd = data.get(0, String.class);
		EzyData params = data.get(1, EzyData.class, null);
		EzyPrototypeSupplier supplier = handlers.get(cmd);
		if(supplier == null) {
			logger.warn("has no handler with command: {}", cmd);
			return;
		}
		EzyHandler handler = (EzyHandler)supplier.supply(beanContext);
		if(handler instanceof EzyUserAware)
			((EzyUserAware)handler).setUser(event.getUser());
		if(handler instanceof EzySessionAware)
			((EzySessionAware)handler).setSession(event.getSession());
		if(handler instanceof EzyDataBinding) {
			if(params != null)
				unmarshaller.unwrap(params, handler);
		}
		prehandle(context, handler);
		try {
			handler.handle();
		}
		catch(EzyBadRequestException e) {
			if(e.isSendToClient()) {
				EzyData errorData = newErrorData(e);
				responseError(context, event, errorData);
			}
			logger.debug("request cmd: " + cmd + " with data: " + data + " error", e);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	protected abstract void responseError(C context, E event, EzyData errorData);
	
	protected void prehandle(C context, EzyHandler handler) {
	}
	
	@SuppressWarnings("rawtypes")
	public abstract static class Builder<B extends Builder>
			extends EzyLoggable
			implements EzyBuilder<EzyUserRequestPrototypeController> {
		
		protected EzyBeanContext beanContext;
		protected EzyPrototypeFactory prototypeFactory;
		protected EzyUnmarshaller unmarshaller;
		
		@SuppressWarnings("unchecked")
		public B beanContext(EzyBeanContext beanContext) {
			this.beanContext = beanContext;
			this.prototypeFactory = beanContext.getPrototypeFactory();
			this.unmarshaller = beanContext.getSingleton("unmarshaller", EzyUnmarshaller.class);
			return (B)this;
		}
		
		private Map<String, EzyPrototypeSupplier> getHandlers() {
			List<EzyPrototypeSupplier> suppliers = filterSuppliers();
			Map<String, EzyPrototypeSupplier> handlers = new ConcurrentHashMap<>();
			for(EzyPrototypeSupplier supplier : suppliers) {
				Class<?> handleType = supplier.getObjectType();
				EzyClientRequestListener annotation = handleType.getAnnotation(EzyClientRequestListener.class);
				String command = EzyClientRequestListenerAnnotations.getCommand(annotation);
				handlers.put(command, supplier);
				logger.debug("add command {} and request handler supplier {}", command, supplier);
			}
			return handlers;
		}
		
		private List<EzyPrototypeSupplier> filterSuppliers() {
			return prototypeFactory.getSuppliers(EzyClientRequestListener.class);
		}
	}
}

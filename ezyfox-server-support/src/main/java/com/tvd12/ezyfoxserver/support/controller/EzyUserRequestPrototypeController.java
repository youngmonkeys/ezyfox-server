package com.tvd12.ezyfoxserver.support.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPrototypeFactory;
import com.tvd12.ezyfox.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;
import com.tvd12.ezyfoxserver.entity.EzyUserAware;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;

public class EzyUserRequestPrototypeController<C extends EzyContext, E extends EzyUserRequestEvent> {

	protected final EzyBeanContext beanContext;
	protected final EzyUnmarshaller unmarshaller;
	protected final Map<String, EzyPrototypeSupplier> handlers;
	
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
		EzyHandler handler = (EzyHandler)supplier.supply(beanContext);
		if(handler instanceof EzyUserAware)
			((EzyUserAware)handler).setUser(event.getUser());
		if(handler instanceof EzySessionAware)
			((EzySessionAware)handler).setSession(event.getSession());
		if(handler instanceof EzyDataBinding)
			if(params != null)
				unmarshaller.unwrap(params, handler);
		prehandle(context, handler);
		handler.handle();
	}
	
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
				String command = annotation.command();
				handlers.put(command, supplier);
				getLogger().debug("add command {} and request handler supplier {}", command, supplier);
			}
			return handlers;
		}
		
		private List<EzyPrototypeSupplier> filterSuppliers() {
			return prototypeFactory.getSuppliers(EzyClientRequestListener.class);
		}
	}
}

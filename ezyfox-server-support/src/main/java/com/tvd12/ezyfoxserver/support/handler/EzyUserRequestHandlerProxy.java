package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

import lombok.Getter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyUserRequestHandlerProxy implements EzyUserRequestHandler {

	@Getter
	protected final Class dataType;
	protected final EzyUserRequestHandler handler;
	
	public EzyUserRequestHandlerProxy(EzyUserRequestHandler handler) {
		this.handler = handler;
		this.dataType = handler.getDataType();
	}
	
	@Override
	public void handle(EzyContext context, EzyUserSessionEvent event, Object data) {
		handler.handle(context, event, data);
	}

}

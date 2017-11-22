package com.tvd12.ezyfoxserver.message.handler;

import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class EzyListMessageHandlers
		extends EzyLoggable
		implements EzyMessageHandlers {

	protected List<EzyMessageHandler> handlers = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Object message) {
		for(EzyMessageHandler handler : handlers)
			handler.handleMessage(message);
	}
	
	@Override
	public void addMessageHandler(EzyMessageHandler handler) {
		this.handlers.add(handler);
	}
	
}

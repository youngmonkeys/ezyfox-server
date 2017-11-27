package com.tvd12.ezyfoxserver.message.handler;

@SuppressWarnings("rawtypes")
public interface EzyMessageHandlers extends EzyMessageHandler {

	void addMessageHandler(EzyMessageHandler handler);
	
}

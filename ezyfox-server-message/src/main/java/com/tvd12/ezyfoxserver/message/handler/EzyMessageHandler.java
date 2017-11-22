package com.tvd12.ezyfoxserver.message.handler;

public interface EzyMessageHandler<M> {

	void handleMessage(M message);
	
}

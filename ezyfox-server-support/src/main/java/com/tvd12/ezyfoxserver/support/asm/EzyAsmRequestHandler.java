package com.tvd12.ezyfoxserver.support.asm;

import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;

@SuppressWarnings("rawtypes")
public interface EzyAsmRequestHandler extends EzyUserRequestHandler {
	
	void setController(Object controller);
	
	void setResponseFactory(EzyResponseFactory responseFactory);
	
	String getCommand();
	
	void setCommand(String command);
	
}

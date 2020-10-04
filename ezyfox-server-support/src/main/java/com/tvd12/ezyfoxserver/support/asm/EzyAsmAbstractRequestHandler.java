package com.tvd12.ezyfoxserver.support.asm;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

import lombok.Getter;
import lombok.Setter;

public abstract class EzyAsmAbstractRequestHandler implements EzyAsmRequestHandler {

	@Getter
	@Setter
	protected String command;
	
	@Override
	public void handle(EzyContext context, EzyUserSessionEvent event, Object data) {
		handleRequest(context, event, data);
	}
	
	public abstract void handleRequest(EzyContext context, EzyUserSessionEvent event, Object data);
	
}

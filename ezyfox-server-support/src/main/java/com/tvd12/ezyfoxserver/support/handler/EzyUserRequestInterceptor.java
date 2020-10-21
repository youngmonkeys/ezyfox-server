package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

public interface EzyUserRequestInterceptor<C extends EzyContext> {
	
	default void preHandle(
			C context, 
			EzyUserSessionEvent event, 
			String command, Object data) {}
	
	default void postHandle(
			C context, 
			EzyUserSessionEvent event, 
			String command, Object data) {}
	
	default void postHandle(
			C context, 
			EzyUserSessionEvent event, 
			String command, Object data, Exception e) {}
	
}

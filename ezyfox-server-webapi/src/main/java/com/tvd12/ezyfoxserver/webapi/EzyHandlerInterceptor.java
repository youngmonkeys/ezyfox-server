package com.tvd12.ezyfoxserver.webapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EzyHandlerInterceptor extends EzyAbstractHandlerInterceptor {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Logger getLogger() {
		return logger;
	}
	
}

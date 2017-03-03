package com.tvd12.ezyfoxserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EzyLoggable {

	protected Logger logger;
	
	{
		logger = LoggerFactory.getLogger(getClass());
	}
	
	protected Logger getLogger() {
		return logger;
	}
	
}

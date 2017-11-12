package com.tvd12.ezyfoxserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EzyLoggable {

	protected transient Logger logger 
			= LoggerFactory.getLogger(getClass());
	
	protected Logger getLogger() {
		return logger;
	}
	
}

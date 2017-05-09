package com.tvd12.ezyfoxserver.bean.impl;

import com.tvd12.ezyfoxserver.bean.EzyErrorHandler;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleErrorHandler 
		extends EzyLoggable 
		implements EzyErrorHandler {

	@Override
	public void handle(Throwable error) {
		getLogger().warn("error", error);
	}
	
}

package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.support.test.exception.RequestException;

@EzyExceptionHandler
public class AppGlobalExceptionHandler extends EzyLoggable {

	@EzyTryCatch(RequestException.class)
	public void handleRequestException(RequestException e) {
		logger.error("try cath", e);
	}
	
}

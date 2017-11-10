package com.tvd12.ezyfoxserver.webapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EzyHandlerInterceptor extends EzyAbstractHandlerInterceptor {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		getLogger().debug("intercept request from {}, with url {}", request.getRemoteAddr(), request.getRequestURI());
		return true;
	}
	
	protected Logger getLogger() {
		return logger;
	}
	
}

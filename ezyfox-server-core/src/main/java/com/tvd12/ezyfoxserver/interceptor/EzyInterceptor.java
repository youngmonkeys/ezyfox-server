package com.tvd12.ezyfoxserver.interceptor;

/**
 * 
 * @author tavandung12
 *
 */
public interface EzyInterceptor<C, D> {
	
	@SuppressWarnings("rawtypes")
	EzyInterceptor EMPTY = (ctx, rev, data) -> {};
	
	void intercept(C ctx, Object rev, D data) throws Exception;
	
}
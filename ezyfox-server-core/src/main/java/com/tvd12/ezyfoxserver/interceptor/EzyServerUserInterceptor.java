package com.tvd12.ezyfoxserver.interceptor;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.exception.NotAuthorizedException;
import com.tvd12.ezyfoxserver.request.EzyUserRequest;

@SuppressWarnings("rawtypes")
public class EzyServerUserInterceptor<D> extends EzyAbstractServerInterceptor<EzyUserRequest> {

	@Override
	public void intercept(EzyServerContext ctx, EzyUserRequest request) throws Exception {
		if(request.getUser() == null)
			throw new NotAuthorizedException("user has not logged in");
	}
	
}

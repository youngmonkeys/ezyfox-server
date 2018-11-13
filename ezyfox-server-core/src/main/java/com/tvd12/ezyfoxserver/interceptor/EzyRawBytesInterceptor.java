package com.tvd12.ezyfoxserver.interceptor;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.exception.EzyNotAuthorizedException;
import com.tvd12.ezyfoxserver.request.EzyStreamingRequest;

public class EzyRawBytesInterceptor extends EzyAbstractServerInterceptor<EzyStreamingRequest> {

	@Override
	public void intercept(EzyServerContext ctx, EzyStreamingRequest request) throws Exception {
		if(request.getUser() == null)
			throw new EzyNotAuthorizedException("user has not logged in");
	}
	
}

package com.tvd12.ezyfoxserver.interceptor;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.exception.NotAuthorizedException;

public class EzyServerUserInterceptor<D> extends EzyAbstractServerInterceptor<D> {

	@Override
	public void intercept(EzyServerContext ctx, Object rev, D data) throws Exception {
		getLogger().debug("intercept user request");
		if(!(rev instanceof EzyUser))
			throw new NotAuthorizedException(rev + " is not an user");
	}
	
}

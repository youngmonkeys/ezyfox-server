package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.context.EzyAppContext;

public interface UserRequestAppHandler<D> 
		extends UserRequestHandler<EzyAppContext, EzyData> {
}

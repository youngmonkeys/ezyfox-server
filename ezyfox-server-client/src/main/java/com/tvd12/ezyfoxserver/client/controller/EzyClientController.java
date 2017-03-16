package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.EzyClientContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyClientController<R> 
		extends EzyController<EzyClientContext, R, EzyArray> {
}

package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.core.annotation.EzyClientRequestInterceptor;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestInterceptor;

@EzyClientRequestInterceptor
public class AppRequestInteceptor 
	implements EzyUserRequestInterceptor<EzyAppContext> {

}

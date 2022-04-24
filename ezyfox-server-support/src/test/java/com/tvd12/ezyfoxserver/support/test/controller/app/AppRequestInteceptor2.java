package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.core.annotation.EzyRequestInterceptor;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestInterceptor;

@EzyRequestInterceptor(priority = 1)
public class AppRequestInteceptor2 
    implements EzyUserRequestInterceptor<EzyAppContext> {

}

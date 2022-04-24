package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyStreamingController;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;

@SuppressWarnings("rawtypes")
public interface EzyServerControllers {

    EzyController getController(EzyConstant cmd);

    EzyInterceptor getInterceptor(EzyConstant cmd);

    EzyInterceptor getStreamingInterceptor();

    EzyStreamingController getStreamingController();
}

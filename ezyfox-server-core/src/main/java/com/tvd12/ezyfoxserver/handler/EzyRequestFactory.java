package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.request.EzySimpleRequest;

@SuppressWarnings("rawtypes")
public interface EzyRequestFactory {

    EzySimpleRequest newRequest(EzyConstant cmd);
}

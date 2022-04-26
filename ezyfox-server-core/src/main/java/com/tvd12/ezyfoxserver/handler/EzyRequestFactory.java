package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.request.EzySimpleRequest;

public interface EzyRequestFactory {

    @SuppressWarnings("rawtypes")
    EzySimpleRequest newRequest(EzyConstant cmd);
}

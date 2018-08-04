package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.request.EzyRequest;

@SuppressWarnings("rawtypes")
public interface EzyRequestFactory {
    
    EzyRequest newRequest(EzyConstant cmd);
    
}

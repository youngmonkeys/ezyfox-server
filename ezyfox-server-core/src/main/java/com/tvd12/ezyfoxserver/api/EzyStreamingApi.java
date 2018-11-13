package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.response.EzyBytesPackage;

public interface EzyStreamingApi {

    void response(EzyBytesPackage pack) throws Exception;
    
}

package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.response.EzyPackage;

public interface EzyResponseApi {

    void response(EzyPackage pack, boolean immediate) throws Exception;
    
    default void response(EzyPackage pack) throws Exception {
        response(pack, false);
    }
    
}

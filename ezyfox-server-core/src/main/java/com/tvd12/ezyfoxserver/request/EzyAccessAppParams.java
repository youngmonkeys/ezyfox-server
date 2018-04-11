package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyAccessAppParams extends EzyRequestParams {

    int getZoneId();
    
    String getAppName();
    
    EzyData getData();
    
}

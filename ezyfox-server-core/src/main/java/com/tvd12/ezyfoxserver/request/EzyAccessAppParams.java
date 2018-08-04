package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyData;

public interface EzyAccessAppParams extends EzyRequestParams {

    String getAppName();
    
    EzyData getData();
    
}

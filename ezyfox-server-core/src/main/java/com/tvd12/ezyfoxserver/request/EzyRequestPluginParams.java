package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

public interface EzyRequestPluginParams extends EzyRequestParams {

    int getPluginId();
    
    EzyArray getData();
    
}

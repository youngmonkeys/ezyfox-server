package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyLoginParams extends EzyRequestParams {

    String getUsername();
    
    String getPassword();
    
    EzyData getData();
    
}

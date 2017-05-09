package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyLoginParams {

    String getUsername();
    
    String getPassword();
    
    EzyArray getData();
    
}

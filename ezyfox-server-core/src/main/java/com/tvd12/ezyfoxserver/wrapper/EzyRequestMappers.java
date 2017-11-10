package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyRequestMappers {

    <T> T toObject(EzyConstant cmd, EzyData data);
    
}

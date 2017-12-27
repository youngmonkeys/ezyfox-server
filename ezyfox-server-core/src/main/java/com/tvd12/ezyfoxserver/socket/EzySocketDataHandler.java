package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzySocketDataHandler {

    public void channelRead(EzyCommand cmd, EzyArray msg) throws Exception;
    
}

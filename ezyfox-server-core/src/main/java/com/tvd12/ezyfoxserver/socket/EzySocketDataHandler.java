package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;

public interface EzySocketDataHandler {

    public void channelRead(EzyCommand cmd, EzyArray msg) throws Exception;
    
}

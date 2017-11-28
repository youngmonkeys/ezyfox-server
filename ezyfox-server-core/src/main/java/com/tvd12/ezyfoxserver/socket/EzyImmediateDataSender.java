package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;

public interface EzyImmediateDataSender {

    void sendDataNow(Object data, EzyTransportType type);
    
}

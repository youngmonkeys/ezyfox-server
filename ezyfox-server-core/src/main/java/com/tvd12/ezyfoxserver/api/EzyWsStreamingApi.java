package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public class EzyWsStreamingApi extends EzyAbstractStreamingApi {

    @Override
    protected String getConnectionType() {
        return EzyConnectionType.WEBSOCKET.getName();
    }
}

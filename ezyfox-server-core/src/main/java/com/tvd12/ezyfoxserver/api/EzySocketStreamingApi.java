package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public class EzySocketStreamingApi extends EzyAbstractStreamingApi {

    @Override
    protected String getConnectionType() {
        return EzyConnectionType.SOCKET.getName();
    }
}

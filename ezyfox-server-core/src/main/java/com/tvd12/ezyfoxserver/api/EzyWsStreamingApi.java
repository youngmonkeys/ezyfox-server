package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public class EzyWsStreamingApi extends EzyAbstractStreamingApi {

    @Override
    protected EzyConstant getConnectionType() {
        return EzyConnectionType.WEBSOCKET;
    }
}

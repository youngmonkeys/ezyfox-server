package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.response.EzyPackage;

public class EzyProxyResponseApi implements EzyResponseApi {

    private final EzyResponseApi websocketResponseApi;
    private final EzyResponseApi socketResponseApi;

    public EzyProxyResponseApi(EzyCodecFactory codecFactory) {
        Object socketEncoder = codecFactory.newEncoder(EzyConnectionType.SOCKET);
        Object wsEncoder = codecFactory.newEncoder(EzyConnectionType.WEBSOCKET);
        this.socketResponseApi = newSocketResponseApi(socketEncoder);
        this.websocketResponseApi = newWebsocketResponseApi(wsEncoder);
    }

    private EzyResponseApi newSocketResponseApi(Object socketEncoder) {
        return socketEncoder != null
            ? createSocketResponseApi(socketEncoder)
            : EzyEmptyResponseApi.getInstance();
    }

    protected EzySocketResponseApi createSocketResponseApi(
        Object socketEncoder
    ) {
        return new EzySocketResponseApi(socketEncoder);
    }

    private EzyResponseApi newWebsocketResponseApi(Object wsEncoder) {
        return wsEncoder != null
            ? new EzyWsResponseApi(wsEncoder)
            : EzyEmptyResponseApi.getInstance();
    }

    @Override
    public void response(EzyPackage pack, boolean immediate) throws Exception {
        socketResponseApi.response(pack, immediate);
        websocketResponseApi.response(pack, immediate);
    }
}

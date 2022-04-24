package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.response.EzyBytesPackage;

public class EzyProxyStreamingApi implements EzyStreamingApi {

    private final EzyStreamingApi websocketResponseApi;
    private final EzyStreamingApi socketResponseApi;

    public EzyProxyStreamingApi() {
        this.socketResponseApi = new EzySocketStreamingApi();
        this.websocketResponseApi = new EzyWsStreamingApi();
    }

    @Override
    public void response(EzyBytesPackage pack) throws Exception {
        socketResponseApi.response(pack);
        websocketResponseApi.response(pack);
    }
}

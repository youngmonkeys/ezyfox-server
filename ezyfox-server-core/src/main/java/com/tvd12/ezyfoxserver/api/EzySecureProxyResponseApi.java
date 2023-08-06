package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;

public class EzySecureProxyResponseApi extends EzyProxyResponseApi {

    public EzySecureProxyResponseApi(EzyCodecFactory codecFactory) {
        super(codecFactory);
    }

    @Override
    protected EzySocketResponseApi createSocketResponseApi(
        Object socketEncoder
    ) {
        return new EzySecureSocketResponseApi(socketEncoder);
    }
}

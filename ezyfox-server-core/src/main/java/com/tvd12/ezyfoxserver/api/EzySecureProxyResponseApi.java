package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;

public class EzySecureProxyResponseApi extends EzyProxyResponseApi {

    public EzySecureProxyResponseApi(
        EzyCodecFactory codecFactory,
        int compressionThreshold
    ) {
        super(codecFactory, compressionThreshold);
    }

    @Override
    protected EzySocketResponseApi createSocketResponseApi(
        Object socketEncoder
    ) {
        return new EzySecureSocketResponseApi(socketEncoder, compressionThreshold);
    }
}

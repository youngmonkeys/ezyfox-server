package com.tvd12.ezyfoxserver.request;

public class EzySimplePingRequest
        extends EzySimpleRequest<EzyRequestParams>
        implements EzyPingRequest {
    private static final long serialVersionUID = -3163515396310239796L;
    
    @Override
    protected EzyRequestParams newParams() {
        return EzySimpleRequestParams.EMPTY;
    }
    
}

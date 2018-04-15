package com.tvd12.ezyfoxserver.request;

public class EzySimplePluginInfoRequest
        extends EzySimpleUserRequest<EzyPluginInfoParams>
        implements EzyPluginInfoRequest {
    private static final long serialVersionUID = -3163515396310239796L;
    
    @Override
    protected EzyPluginInfoParams newParams() {
        return new EzySimplePluginInfoParams();
    }
    
}

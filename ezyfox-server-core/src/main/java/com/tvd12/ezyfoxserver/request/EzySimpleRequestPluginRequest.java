package com.tvd12.ezyfoxserver.request;

public class EzySimpleRequestPluginRequest
        extends EzySimpleUserRequest<EzyRequestPluginParams>
        implements EzyRequestPluginRequest {
    private static final long serialVersionUID = -1501988634117024977L;
    
    @Override
    protected EzyRequestPluginParams newParams() {
        return new EzySimpleRequestPluginParams();
    }
    
}

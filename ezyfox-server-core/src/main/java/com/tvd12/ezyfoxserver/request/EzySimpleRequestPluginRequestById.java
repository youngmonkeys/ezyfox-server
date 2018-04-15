package com.tvd12.ezyfoxserver.request;

public class EzySimpleRequestPluginRequestById
        extends EzySimpleUserRequest<EzyRequestPluginByIdParams>
        implements EzyRequestPluginRequest<EzyRequestPluginByIdParams> {
    private static final long serialVersionUID = -1501988634117024977L;
    
    @Override
    protected EzyRequestPluginByIdParams newParams() {
        return new EzySimpleRequestPluginByIdParams();
    }
    
}

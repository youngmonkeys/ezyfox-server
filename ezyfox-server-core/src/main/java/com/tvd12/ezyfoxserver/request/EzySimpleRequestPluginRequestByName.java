package com.tvd12.ezyfoxserver.request;

public class EzySimpleRequestPluginRequestByName
        extends EzySimpleUserRequest<EzyRequestPluginByNameParams>
        implements EzyRequestPluginRequest<EzyRequestPluginByNameParams> {
    private static final long serialVersionUID = -1501988634117024977L;
    
    @Override
    protected EzyRequestPluginByNameParams newParams() {
        return new EzySimpleRequestPluginByNameParams();
    }
    
}

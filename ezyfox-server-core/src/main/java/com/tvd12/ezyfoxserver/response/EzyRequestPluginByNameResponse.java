package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyRequestPluginByNameResponse 
        extends EzySimpleParamsResponse<EzyRequestPluginResponseParams> {
    private static final long serialVersionUID = 4124109074522892904L;

    public EzyRequestPluginByNameResponse(EzyRequestPluginResponseParams params) {
        super(EzyCommand.PLUGIN_REQUEST_BY_NAME, params);
    }
	
}

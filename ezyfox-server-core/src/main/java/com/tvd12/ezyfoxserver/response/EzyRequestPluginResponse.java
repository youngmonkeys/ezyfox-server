package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyRequestPluginResponse 
        extends EzySimpleParamsResponse<EzyRequestPluginResponseParams> {
    private static final long serialVersionUID = 4124109074522892904L;

    public EzyRequestPluginResponse(EzyRequestPluginResponseParams params) {
        super(EzyCommand.PLUGIN_REQUEST, params);
    }
	
}

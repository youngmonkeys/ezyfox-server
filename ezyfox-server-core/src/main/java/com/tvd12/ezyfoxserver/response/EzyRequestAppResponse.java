package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyRequestAppResponse 
        extends EzySimpleParamsResponse<EzyRequestAppParams> {
    private static final long serialVersionUID = 5054525214862464171L;
    
    public EzyRequestAppResponse(EzyRequestAppParams params) {
        super(EzyCommand.APP_REQUEST, params);
    }
	
}

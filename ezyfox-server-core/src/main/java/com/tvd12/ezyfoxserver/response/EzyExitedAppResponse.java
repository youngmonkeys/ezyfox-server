package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyExitedAppResponse 
        extends EzySimpleParamsResponse<EzyExitedAppParams> {
    private static final long serialVersionUID = 1179311642566987837L;
    
    public EzyExitedAppResponse(EzyExitedAppParams params) {
        super(EzyCommand.APP_EXIT, params);
    }

}

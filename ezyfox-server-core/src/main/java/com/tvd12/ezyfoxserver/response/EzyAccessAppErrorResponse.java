package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyAccessAppErrorResponse 
        extends EzySimpleParamsResponse<EzyErrorParams> {
    private static final long serialVersionUID = -572451803020716190L;

    public EzyAccessAppErrorResponse(EzyErrorParams params) {
        super(EzyCommand.APP_ACCESS_ERROR, params);
    }
    
}

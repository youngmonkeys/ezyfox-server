package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyErrorResponse 
        extends EzySimpleParamsResponse<EzyErrorParams> {
    private static final long serialVersionUID = -657770402301316673L;

    public EzyErrorResponse(EzyErrorParams params) {
        super(EzyCommand.ERROR, params);
    }

}

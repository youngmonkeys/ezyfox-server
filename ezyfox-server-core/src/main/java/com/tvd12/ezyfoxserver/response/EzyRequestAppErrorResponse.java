package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyRequestAppErrorResponse
    extends EzySimpleParamsResponse<EzyErrorParams> {
    private static final long serialVersionUID = -6532549879434016489L;

    public EzyRequestAppErrorResponse(EzyErrorParams params) {
        super(EzyCommand.APP_REQUEST_ERROR, params);
    }
}

package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyLoginErrorResponse
    extends EzySimpleParamsResponse<EzyErrorParams> {
    private static final long serialVersionUID = 3036809112173122255L;

    public EzyLoginErrorResponse(EzyErrorParams params) {
        super(EzyCommand.LOGIN_ERROR, params);
    }

}

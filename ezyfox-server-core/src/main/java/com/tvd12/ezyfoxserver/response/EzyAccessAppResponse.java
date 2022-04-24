package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyAccessAppResponse
    extends EzySimpleParamsResponse<EzyAccessAppParams> {
    private static final long serialVersionUID = 1179311642566987837L;

    public EzyAccessAppResponse(EzyAccessAppParams params) {
        super(EzyCommand.APP_ACCESS, params);
    }
}

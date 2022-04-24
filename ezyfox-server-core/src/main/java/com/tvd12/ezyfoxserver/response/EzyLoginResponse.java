package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyLoginResponse 
        extends EzySimpleParamsResponse<EzyLoginParams> {
    private static final long serialVersionUID = -6999045776866818316L;
    
    public EzyLoginResponse(EzyLoginParams params) {
        super(EzyCommand.LOGIN, params);
    }

}

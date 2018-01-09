package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyPongResponse extends EzySimpleResponse {
    private static final long serialVersionUID = -8041496097838048962L;

    public EzyPongResponse() {
        super(EzyCommand.PONG);
    }

}

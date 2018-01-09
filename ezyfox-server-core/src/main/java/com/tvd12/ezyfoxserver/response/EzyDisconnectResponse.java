package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyDisconnectResponse 
        extends EzySimpleParamsResponse<EzyDisconnectParams> {
    private static final long serialVersionUID = -5720385557675923841L;
    
    public EzyDisconnectResponse(EzyDisconnectParams params) {
	    super(EzyCommand.DISCONNECT, params);
	}
	
}

package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyPingRequest;
import com.tvd12.ezyfoxserver.response.EzyPongResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyPingController 
        extends EzyAbstractServerController 
        implements EzyServerController<EzyPingRequest> {

    @Override
    public void handle(EzyServerContext ctx, EzyPingRequest request) {
        EzyResponse response = EzyPongResponse.getInstance();
        EzySession session = request.getSession();
        ctx.send(response, session, false);
    }
    
}

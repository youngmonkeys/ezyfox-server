package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequest;

public abstract class EzyUserDataHandler<S extends EzySession> 
        extends EzySessionDataHandler<S> {

    public EzyUserDataHandler(EzyServerContext ctx, S session) {
        super(ctx, session);
    }

    private EzyRequestFactory requestFactory = newRequestFactory();
    
    protected void checkToUnmapUser(EzyConstant reason) {
        if(user != null)
            userManager.unmapSessionUser(session, reason);
    }
    
    private EzyRequestFactory newRequestFactory() {
        return new EzySimpleRequestFactory();
    }
    
    @SuppressWarnings({ "rawtypes" })
    protected EzyRequest newRequest(EzyConstant cmd, EzyArray data) {
        EzySimpleRequest request = requestFactory.newRequest(cmd);
        request.setSession(session);
        request.setUser(user);
        request.deserializeParams(data);
        return request;
    }
    
}
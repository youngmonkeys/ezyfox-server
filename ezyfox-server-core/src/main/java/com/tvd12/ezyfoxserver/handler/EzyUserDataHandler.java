package com.tvd12.ezyfoxserver.handler;

import static com.tvd12.ezyfox.util.EzyIfElse.withIf;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;
import com.tvd12.ezyfoxserver.entity.EzyUserAware;
import com.tvd12.ezyfoxserver.request.EzyRequest;
import com.tvd12.ezyfoxserver.request.EzyRequestParamsDeserializable;

public abstract class EzyUserDataHandler<S extends EzySession> 
        extends EzySessionDataHandler<S> {

    public EzyUserDataHandler(EzyServerContext ctx, S session) {
        super(ctx, session);
    }

    private EzyRequestFactory requestFactory = newRequestFactory();
    
    protected void unmapUser() {
        userManager.unmapSessionUser(session);
    }
    
    protected void chechToUnmapUser() {
        withIf(user != null, this::unmapUser);
    }
    
    private EzyRequestFactory newRequestFactory() {
        return new EzySimpleRequestFactory();
    }
    
    @SuppressWarnings({ "rawtypes" })
    protected EzyRequest newRequest(EzyConstant cmd, EzyArray data) {
        EzyRequest request = requestFactory.newRequest(cmd);
        ((EzySessionAware)request).setSession(session);
        ((EzyRequestParamsDeserializable)request).deserializeParams(data);
        if(request instanceof EzyUserAware) {
            ((EzyUserAware)request).setUser(user);
        }
        return request;
    }
    
    protected EzyCloseSession newDisconnectSession(EzyConstant reason) {
        return context.get(EzyCloseSession.class)
                .reason(reason)
                .session(session);
    }
    
}
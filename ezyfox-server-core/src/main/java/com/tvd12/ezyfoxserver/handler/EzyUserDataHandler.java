package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.command.EzyDisconnectSession;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.util.EzyIfElse;

public abstract class EzyUserDataHandler<S extends EzySession> 
        extends EzySessionDataHandler<S> {

    private EzyRequestFactory requestFactory = newRequestFactory();
    
    protected void unmapUser() {
        userManager.unmapSessionUser(session);
        user.removeSession(session);
    }
    
    protected void chechToUnmapUser() {
        EzyIfElse.withIf(user != null, this::unmapUser);
    }
    
    protected Object newRequest(EzyConstant cmd, Object params) {
        return requestFactory.newRequest(cmd, params);
    }
    
    protected EzyDisconnectSession newDisconnectSession(EzyConstant reason) {
        return context.get(EzyDisconnectSession.class)
                .reason(reason)
                .session(session);
    }
    
    private EzyRequestFactory newRequestFactory() {
        return EzyRequestFactory.builder()
                .userSupplier(() -> user)
                .sessionSupplier(() -> session)
                .build();
    }
    
}
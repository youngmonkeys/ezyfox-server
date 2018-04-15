package com.tvd12.ezyfoxserver.handler;

import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyHasSessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public abstract class EzySessionDataHandler<S extends EzySession> 
        extends EzyAbstractDataHandler<S>
        implements EzySessionDelegate {

    protected void provideSession(Supplier<S> sessionSupplier) {
        provideSession0(sessionSupplier);
        updateSession();
    }
    
    protected void provideSession0(Supplier<S> sessionSupplier) {
        session = sessionSupplier.get();
    }
    
    protected void updateSession() {
        session.setMaxIdleTime(sessionManagementSetting.getSessionMaxIdleTime());
        session.setMaxWaitingTime(sessionManagementSetting.getSessionMaxWaitingTime());
        ((EzyHasSessionDelegate)session).setDelegate(this);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void onSessionLoggedIn(EzyUser user) {
        this.user = user;
        this.sessionManager.addLoggedInSession(session);
        this.userManager = getUserManager(user.getZoneId());
        this.zoneContext = context.getZoneContext(user.getZoneId());
    }
    
}

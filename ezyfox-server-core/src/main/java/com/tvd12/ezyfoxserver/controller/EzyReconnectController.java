package com.tvd12.ezyfoxserver.controller;

import static com.tvd12.ezyfoxserver.context.EzyContexts.containsUser;

import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserReconnectEventImpl;
import com.tvd12.ezyfoxserver.request.EzyReconnectRequest;
import com.tvd12.ezyfoxserver.util.EzyIfElse;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyReconnectController
        extends EzyAbstractServerController 
        implements EzyServerController<EzyReconnectRequest>{

    @Override
    public void handle(EzyServerContext ctx, EzyReconnectRequest request) {
        EzySession newsession = request.getSession();
        EzySession oldsession = request.getOldSession();
        EzyServerUserManager userManager = getUserManager(ctx);
        EzySessionManager<EzySession> sessionManager = getSessionManager(ctx);
        EzyUser user = userManager.findAndUpdateUser(oldsession, newsession);
        sessionManager.returnSession(oldsession, EzyDisconnectReason.ANOTHER_SESSION_LOGIN);
        EzyIfElse.withIf(user != null, () -> processUser(ctx, user, newsession));
    }
    
    protected void processUser(EzyServerContext ctx, EzyUser user, EzySession newsession) {
        ctx.get(EzyFireAppEvent.class)
            .filter(appCtx -> containsUser(appCtx, user))
            .fire(EzyEventType.USER_RECONNECT, newReconnectEvent(user, newsession));
    }
    
    protected EzyEvent newReconnectEvent(EzyUser user, EzySession session) {
        return EzyUserReconnectEventImpl.builder().user(user).session(session).build();
    }
    
}

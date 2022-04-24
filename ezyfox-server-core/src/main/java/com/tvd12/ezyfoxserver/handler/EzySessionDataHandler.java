package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public abstract class EzySessionDataHandler<S extends EzySession>
    extends EzyAbstractDataHandler<S> {

    public EzySessionDataHandler(EzyServerContext ctx, S session) {
        super(ctx, session);
    }

    @Override
    public void onSessionLoggedIn(EzyUser user) {
        this.user = user;
        this.userManager = getUserManager(user.getZoneId());
        this.zoneContext = context.getZoneContext(user.getZoneId());
    }

}

package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.request.EzyExitAppRequest;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public class EzyExitAppController 
        extends EzyAbstractServerController 
        implements EzyServerController<EzyExitAppRequest> {

    @Override
    public void handle(EzyServerContext ctx, EzyExitAppRequest request) {
        int appId = request.getParams().getAppId();
        EzyAppContext appContext = ctx.getAppContext(appId);
        EzyApplication app = appContext.getApp();
        EzyAppUserManager userManager = app.getUserManager();
        userManager.removeUser(request.getUser());
        EzyEvent event = new EzySimpleUserRemovedEvent(
                request.getUser(), 
                EzyUserRemoveReason.EXIT_APP);
        appContext.handleEvent(EzyEventType.USER_REMOVED, event);
    }
    
}

package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserExitedAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserExitedAppEvent;
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
        EzyUser user = request.getUser();
        userManager.removeUser(user, EzyUserRemoveReason.EXIT_APP);
        EzyUserExitedAppEvent event = new EzySimpleUserExitedAppEvent(user);
        appContext.handleEvent(EzyEventType.USER_EXITED_APP, event);
    }
}

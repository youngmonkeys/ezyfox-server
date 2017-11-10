package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyUserAccessAppEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserAccessAppEventImpl;
import com.tvd12.ezyfoxserver.event.impl.EzyUserJoinedAppEventImpl;
import com.tvd12.ezyfoxserver.request.EzyAccessAppParams;
import com.tvd12.ezyfoxserver.request.EzyAccessAppRequest;
import com.tvd12.ezyfoxserver.response.EzyAccessAppResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public class EzyAccessAppController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyAccessAppRequest> {

	@Override
	public void handle(EzyServerContext ctx, EzyAccessAppRequest request) {
	    EzyAccessAppParams params = request.getParams();
	    EzyAppContext appContext = ctx.getAppContext(params.getAppName());
	    EzyApplication app = appContext.getApp();
	    EzyAppSetting appSetting = app.getSetting();
	    EzyAppUserManager appUserManger = app.getUserManager();
	    EzyUser user = request.getUser();
	    addUser(appUserManger, user, appSetting);
	    EzyUserAccessAppEvent accessAppEvent = newAccessAppEvent(user);
	    appContext.get(EzyFireEvent.class).fire(EzyEventType.USER_ACCESS_APP, accessAppEvent);
        EzyData ouput = accessAppEvent.getOutput();
        EzySession session = request.getSession();
        response(ctx, session, newAccessAppResponse(appSetting, ouput));
        EzyEvent joinedAppEvent = newJoinedAppEvent(user, session);
        appContext.get(EzyFireEvent.class).fire(EzyEventType.USER_JOINED_APP, joinedAppEvent);
	}
	
	protected void addUser(EzyAppUserManager appUserManger, EzyUser user, EzyAppSetting setting) {
	    if(appUserManger.addUser(user) != null)
            throw new IllegalStateException("user " + user.getName() + " has already joined app " + setting.getName());
	}
	
	protected EzyEvent newJoinedAppEvent(EzyUser user, EzySession session) {
        return EzyUserJoinedAppEventImpl.builder().user(user).session(session).build();
    }
	
	protected EzyUserAccessAppEvent newAccessAppEvent(EzyUser user) {
	    return EzyUserAccessAppEventImpl.builder().user(user).build();
	}
	
	protected EzyResponse newAccessAppResponse(EzyAppSetting app, EzyData out) {
	    return EzyAccessAppResponse.builder().app(app).data(out).build();
	}
	
}

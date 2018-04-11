package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyIAccessAppError;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyUserAccessAppEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleUserAccessAppEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleUserJoinedAppEvent;
import com.tvd12.ezyfoxserver.exception.EzyAccessAppException;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;
import com.tvd12.ezyfoxserver.request.EzyAccessAppParams;
import com.tvd12.ezyfoxserver.request.EzyAccessAppRequest;
import com.tvd12.ezyfoxserver.response.EzyAccessAppErrorResponse;
import com.tvd12.ezyfoxserver.response.EzyAccessAppResponse;
import com.tvd12.ezyfoxserver.response.EzyErrorParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public class EzyAccessAppController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyAccessAppRequest> {

	@Override
	public void handle(EzyServerContext ctx, EzyAccessAppRequest request) {
	    EzyAccessAppParams params = request.getParams();
	    EzyZoneContext zoneContext = ctx.getZoneContext(params.getZoneId());
	    EzyAppContext appContext = zoneContext.getAppContext(params.getAppName());
	    EzyApplication app = appContext.getApp();
	    EzyAppSetting appSetting = app.getSetting();
	    EzyAppUserManager appUserManger = app.getUserManager();
	    EzyUser user = request.getUser();
	    EzySession session = request.getSession();
	    try {
	        addUser(appUserManger, user, appSetting);
	    }
	    catch(EzyAccessAppException e) {
	        responseAccessAppError(ctx, session, e);
	        throw e;
	    }
	    EzyUserAccessAppEvent accessAppEvent = newAccessAppEvent(user);
	    appContext.get(EzyFireEvent.class).fire(EzyEventType.USER_ACCESS_APP, accessAppEvent);
        EzyData ouput = accessAppEvent.getOutput();
        response(ctx, session, newAccessAppResponse(appSetting, ouput));
        EzyEvent joinedAppEvent = newJoinedAppEvent(user, session);
        appContext.get(EzyFireEvent.class).fire(EzyEventType.USER_JOINED_APP, joinedAppEvent);
	}
	
	protected void addUser(EzyAppUserManager appUserManger, EzyUser user, EzyAppSetting setting) {
	    try {
	        addUser0(appUserManger, user, setting);
	    }
	    catch(EzyMaxUserException e) {
	        throw EzyAccessAppException.maximumUser(setting.getName(), e);
	    }
	}
	
	protected void addUser0(EzyAppUserManager appUserManger, EzyUser user, EzyAppSetting setting) {
        if(appUserManger.addUser(user) != null)
            throw EzyAccessAppException.hasJoinedApp(user.getName(), setting.getName());
    }
	
	protected EzyEvent newJoinedAppEvent(EzyUser user, EzySession session) {
        return EzySimpleUserJoinedAppEvent.builder().user(user).session(session).build();
    }
	
	protected EzyUserAccessAppEvent newAccessAppEvent(EzyUser user) {
	    return EzySimpleUserAccessAppEvent.builder().user(user).build();
	}
	
	protected EzyResponse newAccessAppResponse(EzyAppSetting app, EzyData out) {
	    com.tvd12.ezyfoxserver.response.EzyAccessAppParams params = 
	            new com.tvd12.ezyfoxserver.response.EzyAccessAppParams();
	    params.setApp(app);
	    params.setData(out);
	    return new EzyAccessAppResponse(params);
	}
	
	protected EzyResponse newAccessAppErrorReponse(EzyIAccessAppError error) {
	    EzyErrorParams params = new EzyErrorParams();
	    params.setError(error);
        return new EzyAccessAppErrorResponse(params);
    }
	
	protected void responseAccessAppError(
	        EzyContext ctx, EzySession session, EzyAccessAppException exception) {
        response(ctx, session, newAccessAppErrorReponse(exception.getError()));
    }
    
}

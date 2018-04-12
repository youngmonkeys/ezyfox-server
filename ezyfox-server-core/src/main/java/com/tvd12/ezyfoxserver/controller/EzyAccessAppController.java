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
import com.tvd12.ezyfoxserver.event.EzySimpleUserAccessAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserAccessAppEvent;
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
	   try {
	       handle0(ctx, request);
	   }
	   catch(EzyAccessAppException e) {
           responseAccessAppError(ctx, request.getSession(), e);
           throw e;
       }
	}
	
	protected void handle0(EzyServerContext ctx, EzyAccessAppRequest request) {
        EzyAccessAppParams params = request.getParams();
        EzyZoneContext zoneContext = ctx.getZoneContext(params.getZoneId());
        EzyAppContext appContext = zoneContext.getAppContext(params.getAppName());
        EzyApplication app = appContext.getApp();
        EzyAppSetting appSetting = app.getSetting();
        EzyAppUserManager appUserManger = app.getUserManager();
        EzyUser user = request.getUser();
        EzySession session = request.getSession();
        checkAppUserMangerAvailable(appUserManger);
        EzyFireEvent fireEvent = appContext.get(EzyFireEvent.class);
        EzyUserAccessAppEvent accessAppEvent = newAccessAppEvent(user);
        fireEvent.fire(EzyEventType.USER_ACCESS_APP, accessAppEvent);
        addUser(appUserManger, user, appSetting);
        EzyData ouput = accessAppEvent.getOutput();
        EzyResponse accessAppResponse = newAccessAppResponse(appSetting, ouput);
        response(ctx, session, accessAppResponse);
    }
	
	protected void checkAppUserMangerAvailable(EzyAppUserManager appUserManger) {
	    int current = appUserManger.getUserCount();
	    int max = appUserManger.getMaxUsers();
	    String appName = appUserManger.getAppName();
	    if(current >= max)
	        throw EzyAccessAppException.maximumUser(appName, current, max);
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

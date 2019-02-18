package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyIAccessAppError;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
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
	    EzyUser user = request.getUser();
	    int zoneId = user.getZoneId();
        EzyAccessAppParams params = request.getParams();
        EzyZoneContext zoneContext = ctx.getZoneContext(zoneId);
        EzyAppContext appContext = zoneContext.getAppContext(params.getAppName());
        EzyApplication app = appContext.getApp();
        EzyAppSetting appSetting = app.getSetting();
        EzyAppUserManager appUserManger = app.getUserManager();
        EzySession session = request.getSession();
        checkAppUserMangerAvailable(appUserManger);
        EzyUserAccessAppEvent accessAppEvent = newAccessAppEvent(user);
        appContext.handleEvent(EzyEventType.USER_ACCESS_APP, accessAppEvent);
        addUser(appUserManger, user, appSetting);
        EzyResponse accessAppResponse = newAccessAppResponse(zoneId, appSetting, accessAppEvent.getOutput());
        ctx.send(accessAppResponse, session);
    }
	
	protected void checkAppUserMangerAvailable(EzyAppUserManager appUserManger) {
	    int current = appUserManger.getUserCount();
	    int max = appUserManger.getMaxUsers();
	    String appName = appUserManger.getAppName();
	    if(current >= max)
	        throw EzyAccessAppException.maximumUser(appName, current, max);
	}
	
	protected void addUser(
	        EzyAppUserManager appUserManger, EzyUser user, EzyAppSetting setting) {
	    try {
	        appUserManger.addUser(user);
	    }
	    catch(EzyMaxUserException e) {
	        throw EzyAccessAppException.maximumUser(setting.getName(), e);
	    }
	}
	
	protected EzyUserAccessAppEvent newAccessAppEvent(EzyUser user) {
	    return new EzySimpleUserAccessAppEvent(user);
	}
	
	protected EzyResponse newAccessAppResponse(int zoneId, EzyAppSetting app, EzyArray data) {
	    com.tvd12.ezyfoxserver.response.EzyAccessAppParams params = 
	            new com.tvd12.ezyfoxserver.response.EzyAccessAppParams();
	    params.setApp(app);
	    params.setData(data);
	    return new EzyAccessAppResponse(params);
	}
	
	protected EzyResponse newAccessAppErrorReponse(EzyIAccessAppError error) {
	    EzyErrorParams params = new EzyErrorParams();
	    params.setError(error);
        return new EzyAccessAppErrorResponse(params);
    }
	
	protected void responseAccessAppError(
	        EzyServerContext ctx, EzySession session, EzyAccessAppException exception) {
	    EzyResponse reponse = newAccessAppErrorReponse(exception.getError());
	    ctx.send(reponse, session);
    }
    
}

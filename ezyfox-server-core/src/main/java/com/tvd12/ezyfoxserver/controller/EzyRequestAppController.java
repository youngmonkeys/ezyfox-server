package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.constant.EzyIRequestAppError;
import com.tvd12.ezyfoxserver.constant.EzyRequestAppError;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;
import com.tvd12.ezyfoxserver.request.EzyRequestAppRequest;
import com.tvd12.ezyfoxserver.response.EzyErrorParams;
import com.tvd12.ezyfoxserver.response.EzyRequestAppErrorResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyRequestAppController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyRequestAppRequest> {

	@Override
	public void handle(EzyServerContext ctx, EzyRequestAppRequest request) {
	    EzyRequestAppParams params = request.getParams();
	    EzyAppContext appCtx = ctx.getAppContext(params.getAppId());
	    EzyApplication app = appCtx.getApp();
	    EzyAppRequestController requestController = app.getRequestController();
	    
	    // user manager for checking, user must be managed
	    EzyUserManager userManger = appCtx.getApp().getUserManager();
	    
	    EzyUser user = request.getUser();
	    
	    // check user joined app or not to prevent spam request
	    boolean hasAccessed = userManger.containsUser(user);
	    
	    if(hasAccessed) {
            // redirect handling to app
	        EzyUserRequestAppEvent event = newRequestAppEvent(request);
            requestController.handle(appCtx, event);
	    }
	    else {
	        EzySession session = request.getSession();
	        responseRequestAppError(ctx, session, EzyRequestAppError.HAS_NOT_ACCESSED);
	    }
	}
	
	protected EzyUserRequestAppEvent newRequestAppEvent(EzyRequestAppRequest request) {
		return new EzySimpleUserRequestAppEvent(
		        request.getUser(),
		        request.getSession(), 
		        request.getParams().getData());
	}
	
	protected EzyResponse newRequestAppErrorReponse(EzyIRequestAppError error) {
        EzyErrorParams params = new EzyErrorParams();
        params.setError(error);
        return new EzyRequestAppErrorResponse(params);
    }
    
    protected void responseRequestAppError(
            EzyServerContext ctx, EzySession session, EzyIRequestAppError error) {
        EzyResponse response = newRequestAppErrorReponse(error);
        ctx.send(response, session);
    }
}

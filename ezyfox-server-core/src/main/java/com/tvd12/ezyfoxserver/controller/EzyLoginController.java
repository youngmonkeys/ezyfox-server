package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyILoginError;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserLoginEventImpl;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.request.EzyLoginParams;
import com.tvd12.ezyfoxserver.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.response.EzyLoginErrorResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyLoginController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyLoginRequest> {

	@Override
	public void handle(EzyServerContext ctx, EzyLoginRequest request) {
	    try {
	        EzySession session = request.getSession();
	        EzyLoginParams params = request.getParams();
            control(ctx, newLoginEvent(session, params));
        }
        catch(EzyLoginErrorException e) {
            processException(ctx, request.getSession(), e);
            throw e;
        }
	}
	
	protected void processException(
	        EzyServerContext ctx, EzySession session, EzyLoginErrorException e) {
	    responseLoginError(ctx, session, e.getError());
	}
	
	protected void control(EzyServerContext ctx, EzyUserLoginEvent event) {
		firePluginEvent(ctx, event);
		process(ctx, event);
	}
	
	protected void process(EzyServerContext ctx, EzyUserLoginEvent event) {
	    EzyLoginProcessor.builder().context(ctx).event(event).build().apply();
	}
	
	protected void firePluginEvent(EzyServerContext ctx, EzyUserLoginEvent event) {
        ctx.get(EzyFirePluginEvent.class).fire(EzyEventType.USER_LOGIN, event);
    }
	
	protected EzyUserLoginEvent newLoginEvent(EzySession session, EzyLoginParams params) {
		return EzyUserLoginEventImpl.builder()
		        .data(params.getData())
				.username(params.getUsername())
				.password(params.getPassword())
				.session(session)
				.build();
	}
	
    protected void responseLoginError(EzyContext ctx, EzySession session, EzyILoginError error) {
        response(ctx, session, newLoginErrorReponse(error));
    }
    
    protected EzyResponse newLoginErrorReponse(EzyILoginError error) {
        return EzyLoginErrorResponse.builder().error(error).build();
    }
    
}

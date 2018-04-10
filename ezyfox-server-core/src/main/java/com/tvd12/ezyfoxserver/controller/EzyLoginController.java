package com.tvd12.ezyfoxserver.controller;

import static com.tvd12.ezyfoxserver.exception.EzyLoginErrorException.maximumUsers;
import static com.tvd12.ezyfoxserver.exception.EzyLoginErrorException.zoneNotFound;

import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyILoginError;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;
import com.tvd12.ezyfoxserver.exception.EzyZoneNotFoundException;
import com.tvd12.ezyfoxserver.request.EzyLoginParams;
import com.tvd12.ezyfoxserver.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.response.EzyErrorParams;
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
	        EzyZoneContext zoneContext = ctx.getZoneContext(params.getZoneName());
	        EzyUserLoginEvent loginEvent = newLoginEvent(session, params);
            control(ctx, zoneContext, loginEvent);
        }
        catch(EzyLoginErrorException e) {
            processException(ctx, request.getSession(), e);
            throw e;
        }
	    catch(EzyMaxUserException e) {
	        processException(ctx, request.getSession(), maximumUsers(e));
	        throw e;
	    }
	    catch(EzyZoneNotFoundException e) {
	        processException(ctx, request.getSession(), zoneNotFound(e));
	        throw e;
	    }
	}
	
	protected void processException(
	        EzyServerContext ctx, EzySession session, EzyLoginErrorException e) {
	    responseLoginError(ctx, session, e.getError());
	}
	
	protected void control(
	        EzyServerContext ctx, EzyZoneContext zoneContext, EzyUserLoginEvent event) {
		firePluginEvent(zoneContext, event);
		process(ctx, zoneContext, event);
	}
	
	protected void process(
	        EzyServerContext ctx, EzyZoneContext zoneContext, EzyUserLoginEvent event) {
	    EzyLoginProcessor processor = EzyLoginProcessor.builder()
	            .event(event)
	            .serverContext(ctx)
	            .zoneContext(zoneContext)
	            .build();
	    processor.apply();
	}
	
	protected void firePluginEvent(EzyZoneContext ctx, EzyUserLoginEvent event) {
        ctx.get(EzyFirePluginEvent.class).fire(EzyEventType.USER_LOGIN, event);
    }
	
	protected EzyUserLoginEvent newLoginEvent(EzySession session, EzyLoginParams params) {
		return EzySimpleUserLoginEvent.builder()
		        .data(params.getData())
		        .zoneName(params.getZoneName())
				.username(params.getUsername())
				.password(params.getPassword())
				.session(session)
				.build();
	}
	
    protected void responseLoginError(EzyContext ctx, EzySession session, EzyILoginError error) {
        response(ctx, session, newLoginErrorReponse(error));
    }
    
    protected EzyResponse newLoginErrorReponse(EzyILoginError error) {
        EzyErrorParams params = new EzyErrorParams();
        params.setError(error);
        return new EzyLoginErrorResponse(params);
    }
    
}

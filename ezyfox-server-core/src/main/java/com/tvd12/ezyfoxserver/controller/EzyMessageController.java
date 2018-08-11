package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfox.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyMessageController extends EzyEntityBuilders {
	
	protected final void response(
	        EzyContext ctx, EzySession session, EzyResponse response) {
	    ctx.get(EzySendResponse.class)
	        .recipient(session)
	        .response(response)
	        .execute();
    }
	
	protected final void responseNow(
	        EzyContext ctx, EzySession session, EzyResponse response) {
        ctx.get(EzySendResponse.class)
            .recipient(session)
            .immediate(true)
            .response(response)
            .execute();
    }
	
}

package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySender;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

public class EzyMessageController extends EzyEntityBuilders {
	
	protected final void response(
	        EzyContext ctx, EzySender sender, EzyResponse response) {
	    ctx.get(EzySendResponse.class)
	        .sender(sender)
	        .response(response)
	        .execute();
    }
	
	protected final void responseNow(
	        EzyContext ctx, EzySender sender, EzyResponse response) {
        ctx.get(EzySendResponse.class)
            .sender(sender)
            .immediate(true)
            .response(response)
            .execute();
    }
	
}

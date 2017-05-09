package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySender;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

public class EzyMessageController extends EzyEntityBuilders {
	
	protected void response(EzyContext ctx, EzySender sender, EzyResponse response) {
	    ctx.get(EzySendResponse.class).sender(sender).response(response).execute();
    }
	
}

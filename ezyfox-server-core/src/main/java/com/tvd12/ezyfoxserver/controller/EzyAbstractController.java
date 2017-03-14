package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyAbstractController extends EzyLoggable {

	protected EzyResponseSerializer getResponseSerializer(EzyContext ctx) {
		return ctx.get(EzyResponseSerializer.class);
	}
	
	protected EzyObject serializeToObject(EzyContext ctx, EzyResponse response) {
		return getResponseSerializer(ctx).serializeToObject(response);
	}
	
	protected EzyArray serializeToArray(EzyContext ctx, EzyResponse response) {
		return getResponseSerializer(ctx).serializeToArray(response);
	}
	
}

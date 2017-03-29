package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyFactory;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyMessageController extends EzyLoggable {

	protected EzyResponseSerializer getResponseSerializer(EzyContext ctx) {
		return ctx.get(EzyResponseSerializer.class);
	}
	
	protected EzyObject serializeToObject(EzyContext ctx, EzyResponse response) {
		return getResponseSerializer(ctx).serializeToObject(response);
	}
	
	protected EzyArray serializeToArray(EzyContext ctx, EzyResponse response) {
		return getResponseSerializer(ctx).serializeToArray(response);
	}
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyFactory.create(EzyArrayBuilder.class);
	}
	
	protected EzyObjectBuilder newObjectBuilder() {
		return EzyFactory.create(EzyObjectBuilder.class);
	}
	
}

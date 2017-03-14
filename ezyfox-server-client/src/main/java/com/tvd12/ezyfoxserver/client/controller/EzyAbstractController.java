package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyFactory;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyAbstractController extends EzyLoggable {

	protected EzyRequestSerializer getRequestSerializer(EzyContext ctx) {
		return ctx.get(EzyRequestSerializer.class);
	}
	
	protected EzyObject serializeToObject(EzyContext ctx, EzyRequest request) {
		return getRequestSerializer(ctx).serializeToObject(request);
	}
	
	protected EzyArray serializeToArray(EzyContext ctx, EzyRequest request) {
		return getRequestSerializer(ctx).serializeToArray(request);
	}
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyFactory.create(EzyArrayBuilder.class);
	}
	
	protected EzyObjectBuilder newObjectBuilder() {
		return EzyFactory.create(EzyObjectBuilder.class);
	}
}

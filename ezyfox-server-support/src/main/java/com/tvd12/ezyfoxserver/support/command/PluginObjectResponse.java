package com.tvd12.ezyfoxserver.support.command;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;

public class PluginObjectResponse extends AbstractObjectResponse {

	protected final Map<Object, Object> additionalParams = new HashMap<>();
	
	public PluginObjectResponse(EzyPluginContext context, EzyMarshaller marshaller) {
		super(context, marshaller);
	}
	
	@Override
	protected EzyResponse newResponse() {
		return context.get(EzyPluginResponse.class);
	}
	
}

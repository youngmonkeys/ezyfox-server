package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;

public class PluginArrayResponse extends AbstractArrayResponse {
	
	public PluginArrayResponse(EzyPluginContext context, EzyMarshaller marshaller) {
		super(context, marshaller);
	}
	
	@Override
	protected EzyResponse newResponse() {
		return context.get(EzyPluginResponse.class);
	}
	
}

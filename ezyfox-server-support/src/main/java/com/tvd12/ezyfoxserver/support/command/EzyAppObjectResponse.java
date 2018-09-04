package com.tvd12.ezyfoxserver.support.command;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.context.EzyAppContext;

public class EzyAppObjectResponse extends EzyAbstractObjectResponse {

	protected final Map<Object, Object> additionalParams = new HashMap<>();
	
	public EzyAppObjectResponse(EzyAppContext context, EzyMarshaller marshaller) {
		super(context, marshaller);
	}
	
	@Override
	protected EzyResponse newResponse() {
		return context.cmd(EzyAppResponse.class);
	}
	
}

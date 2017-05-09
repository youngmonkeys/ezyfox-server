package com.tvd12.ezyfoxserver.client.cmd.impl;

import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.command.EzyShutdown;

public class EzyClientShutdownImpl implements EzyShutdown {

	private EzyClientContext context;
	
	public EzyClientShutdownImpl(EzyClientContext context) {
		this.context = context;
	}
	
	@Override
	public Boolean execute() {
		context.destroy();
		return Boolean.TRUE;
	}

	
	
}

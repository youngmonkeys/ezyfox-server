package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.context.EzyServerContext;

import lombok.Setter;

public class EzyEmptyHttpBootstrap implements EzyHttpBootstrap {

	@Setter
	protected EzyServerContext serverContext;
	
	@Override
	public void start() throws Exception {}

	@Override
	public void destroy() {}

}

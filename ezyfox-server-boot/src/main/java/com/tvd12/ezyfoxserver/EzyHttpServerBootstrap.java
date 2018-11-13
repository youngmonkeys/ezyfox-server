package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;

public abstract class EzyHttpServerBootstrap extends EzyServerBootstrap {

	private EzyHttpBootstrap httpBootstrap;
	
	protected void startHttpBootstrap() throws Exception {
		EzyHttpSetting setting = getHttpSetting();
		if(!setting.isActive()) return;
	    logger.debug("starting http server bootstrap ....");
	    httpBootstrap = newHttpBottstrap();
	    httpBootstrap.start();
	    logger.debug("http server bootstrap has started");
	}
	
	private EzyHttpBootstrap newHttpBottstrap() {
		EzyComplexHttpBootstrap bootstrap = new EzyComplexHttpBootstrap();
		bootstrap.setServerContext(context);
		return bootstrap;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(httpBootstrap != null)
			processWithLogException(httpBootstrap::destroy);
	}
	
}

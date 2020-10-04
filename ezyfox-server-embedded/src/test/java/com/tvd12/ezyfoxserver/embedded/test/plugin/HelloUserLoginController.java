package com.tvd12.ezyfoxserver.embedded.test.plugin;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyServerEventHandler;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;

@EzySingleton
@EzyServerEventHandler(EzyEventNames.USER_LOGIN)
public class HelloUserLoginController 
		extends EzyAbstractPluginEventController<EzyUserLoginEvent> {

	@Override
	public void handle(EzyPluginContext ctx, EzyUserLoginEvent event) {
		logger.info("HELLO - user {} login in", event.getUsername());
		if(event.getUsername().contains("admin"))
			throw new EzyLoginErrorException(EzyLoginError.INVALID_USERNAME);
	}

}
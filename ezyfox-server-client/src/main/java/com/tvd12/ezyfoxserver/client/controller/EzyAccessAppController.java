package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.controller.EzyServerController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzyAccessAppController 
		extends EzyAbstractController 
		implements EzyServerController<EzyUser> {

	@Override
	public void handle(EzyContext ctx, EzyUser session, EzyArray data) {
		getLogger().info("access app success, appId = " + data.get(0));
	}

}

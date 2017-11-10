package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyDisconnectController 
		extends EzyAbstractController 
		implements EzyClientController<EzyClientUser> {

	@Override
	public void handle(EzyClientContext ctx, EzyClientUser rev, EzyArray data) {
		getLogger().info("user be disconnected by reason {}", 
				EzyDisconnectReason.valueOf(data.get(0, Integer.class)));
	}
	
}

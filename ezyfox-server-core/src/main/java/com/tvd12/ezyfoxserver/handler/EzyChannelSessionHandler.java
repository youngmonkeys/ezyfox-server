package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;

import lombok.Setter;

public class EzyChannelSessionHandler extends EzyChannelDataHandler {

	@Setter
	protected EzyControllers controllers;
	
	@Override
	protected void sessionAdded(EzySession session) {
		getLogger().info("add session: {}", session);
	}

	@Override
	protected void dataReceived(EzySession session, EzyArray msg) {
		getLogger().info("from session: {}", session);
		int appId = msg.get(0);
		EzyConstant cmd = EzyCommand.valueOf(msg.get(1, int.class));
//		EzyController<EzyData> controller = controllers.getController(cmd);
//		EzyData data = msg.get(2, controller.getDataType());
		getLogger().info("appId {}, command {}, data {}", appId, cmd);
//		controller.handle(appId, data);
	}

}

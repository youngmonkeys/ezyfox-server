package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyServerController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;

import lombok.Setter;

public class EzyChannelSessionHandler extends EzyChannelDataHandler {

	@Setter
	protected EzyServerContext context;
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
		int cmdId = msg.get(1);
		EzyData data = msg.get(2);
		EzyConstant cmd = EzyCommand.valueOf(cmdId);
		handleRequest(appId, cmd, data);
		getLogger().info("appId {}, command {}, data {}", appId, cmd, data);
	}
	
	protected void handleRequest(int appId, EzyConstant cmd, EzyData data) {
		handleRequest(appId, controllers.getController(cmd), data);
	}
	
	protected void handleRequest(int appId, EzyController controller, EzyData data) {
		if(appId < 0)
			handleServerRequest(controller, data);
	}
	
	protected void handleServerRequest(EzyController controller, EzyData data) {
		((EzyServerController)controller).handle(context, session, (EzyArray)data);
	}
	
	protected EzyAppContext getAppContext(int appId) {
		return context.getAppContext(appId);
	}

}

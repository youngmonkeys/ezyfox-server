package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
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
		getLogger().debug("add session: {}", session);
	}

	@Override
	protected void dataReceived(EzySession session, EzyArray msg) {
		getLogger().debug("from session: {}", session);
		int appId = msg.get(0);
		int cmdId = msg.get(1);
		EzyData data = msg.get(2);
		EzyConstant cmd = EzyCommand.valueOf(cmdId);
		handleRequest(appId, cmd, data);
		getLogger().debug("appId {}, command {}, data {}", appId, cmd, data);
	}
	
	protected void handleRequest(int appId, EzyConstant cmd, EzyData data) {
		getContext(appId).get(EzyRunWorker.class).run(() -> {
			tryHandleRequest(appId, cmd, data);
		});
	}
	
	protected void tryHandleRequest(int appId, EzyConstant cmd, EzyData data) {
		try {
			handleRequest(appId, controllers.getController(cmd), data);
		}
		catch(Exception e) {
			throw new IllegalStateException(newHandleRequestErrorMessage(appId, cmd, data), e);
		}
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
	
	protected EzyContext getContext(int appId) {
		return appId < 0 ? context : context.getAppContext(appId);
	}
	
	protected String newHandleRequestErrorMessage(int appId, EzyConstant cmd, EzyData data) {
		return "error when handle request app: " + appId + ", command: " + cmd + ", data: " + data;
	}

}

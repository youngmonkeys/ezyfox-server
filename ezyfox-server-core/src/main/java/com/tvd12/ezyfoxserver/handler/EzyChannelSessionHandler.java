package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyRequestHandleException;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import lombok.Setter;

public abstract class EzyChannelSessionHandler extends EzyChannelDataHandler {

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
			throw new EzyRequestHandleException(newHandleRequestErrorMessage(appId, cmd, data), e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void handleRequest(int appId, EzyController controller, EzyData data) {
		if(appId < 0)
			handleServerRequest(controller, data);
		else
			handleAppRequest(controller, data);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void handleServerRequest(EzyController controller, EzyData data) {
		controller.handle(context, getReceiver(), data);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void handleAppRequest(EzyController controller, EzyData data) {
		controller.handle(context, getReceiver(), data);
	}
	
	protected Object getReceiver() {
		return session;
	}
	
	protected EzyAppContext getAppContext(int appId) {
		return context.getAppContext(appId);
	}
	
	protected EzyContext getContext(int appId) {
		return appId < 0 ? context : context.getAppContext(appId);
	}
	
	public void setContext(EzyServerContext ctx) {
		this.context = ctx;
		this.controllers = getBoss().getControllers();
		this.sessionManager = getManagers().getManager(EzySessionManager.class);
	}
	
	protected EzyServer getBoss() {
		return context.getBoss();
	}
	
	protected EzyManagers getManagers() {
		return getBoss().getManagers();
	}
	
	protected String newHandleRequestErrorMessage(int appId, EzyConstant cmd, EzyData data) {
		return "error when handle request app: " + appId + ", command: " + cmd + ", data: " + data;
	}

}

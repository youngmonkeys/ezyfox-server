package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyRequestHandleException;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import lombok.Setter;

public class EzySessionHandler extends EzyDataHandler {

	@Setter
	protected EzyControllers controllers;

	@Override
	protected void sessionAdded(EzySession session) {
		getLogger().debug("add session: {}", session);
	}

	@Override
	protected void dataReceived(EzySession session, EzyArray msg) {
		getLogger().debug("from session: {}", session);
		int cmdId = msg.get(0);
		EzyData data = msg.get(1);
		EzyConstant cmd = EzyCommand.valueOf(cmdId);
		handleRequest(cmd, data);
		getLogger().debug("command {}, data {}", cmd, data);
	}
	
	protected void handleRequest(EzyConstant cmd, EzyData data) {
		context.get(EzyRunWorker.class).run(() ->
			tryHandleRequest(cmd, data)
		);
	}
	
	protected void tryHandleRequest(EzyConstant cmd, EzyData data) {
		try {
			interceptRequest(controllers.getInterceptor(cmd), data);
			handleRequest(controllers.getController(cmd), data);
		}
		catch(Exception e) {
			throw new EzyRequestHandleException(newHandleRequestErrorMessage(cmd, data), e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void interceptRequest(EzyInterceptor interceptor, EzyData data) 
			throws Exception {
		interceptServerRequest(interceptor, data);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void interceptServerRequest(EzyInterceptor interceptor, EzyData data) 
			throws Exception {
		interceptor.intercept(context, getReceiver(), data);
	}
	
	@SuppressWarnings("rawtypes")
	protected void handleRequest(EzyController controller, EzyData data) {
		handleServerRequest(controller, data);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void handleServerRequest(EzyController controller, EzyData data) {
		controller.handle(context, getReceiver(), data);
	}
	
	protected Object getReceiver() {
		return session;
	}
	
	protected EzyAppContext getAppContext(int appId) {
		return context.getAppContext(appId);
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
	
	protected String newHandleRequestErrorMessage(EzyConstant cmd, EzyData data) {
		return "error when handle request command: " + cmd + ", data: " + data;
	}
	
}

package com.tvd12.ezyfoxserver.client;

import java.util.Set;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.controller.EzyClientAppController;
import com.tvd12.ezyfoxserver.client.listener.EzyClientAppResponseListener;
import com.tvd12.ezyfoxserver.client.wrapper.EzyClientAppControllers;
import com.tvd12.ezyfoxserver.client.wrapper.EzyClientAppResponseListeners;
import com.tvd12.ezyfoxserver.client.wrapper.EzyClientControllers;
import com.tvd12.ezyfoxserver.client.wrapper.impl.EzyClientAppControllersImpl;
import com.tvd12.ezyfoxserver.client.wrapper.impl.EzyClientAppResponseListenersImpl;
import com.tvd12.ezyfoxserver.client.wrapper.impl.EzyClientControllersImpl;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public class EzyClient implements EzyDestroyable {

	protected EzyClientControllers controllers = newControllers();
	protected EzyClientAppControllers appControllers = newAppControllers();
	protected EzyClientAppResponseListeners appResponseListeners = newAppResponseListeners();
	protected Set<EzyConstant> unloggableCommands = defaultUnloggableCommands();
	
	@SuppressWarnings({ "unchecked" })
	public <T> T getController(EzyConstant cmd) {
		return (T)controllers.getController(cmd);
	}
	
	public void addController(EzyConstant cmd, Object ctrl) {
		controllers.addController(cmd, ctrl);
	}
	
	@SuppressWarnings("rawtypes")
	public EzyClientAppController getAppController(EzyConstant cmd) {
		return appControllers.getController(cmd);
	}
	
	@SuppressWarnings("rawtypes")
	public void addAppController(EzyConstant cmd, EzyClientAppController ctrl) {
		appControllers.addController(cmd, ctrl);
	}
	
	@SuppressWarnings("rawtypes")
	public EzyClientAppResponseListener getAppResponseListener(Object requestId) {
		return appResponseListeners.getListener(requestId);
	}
	
	@SuppressWarnings("rawtypes")
	public void addClientAppResponseListener(
			Object requestId, EzyClientAppResponseListener listener) {
		appResponseListeners.addListener(requestId, listener);
	}
	
	public Set<EzyConstant> getUnloggableCommands() {
		return unloggableCommands;
	}
	
	@Override
	public void destroy() {
		controllers.destroy();
		controllers = null;
	}
	
	protected EzyClientControllers newControllers() {
		return EzyClientControllersImpl.builder().build();
	}
	
	protected EzyClientAppControllers newAppControllers() {
		return EzyClientAppControllersImpl.builder().build();
	}
	
	protected EzyClientAppResponseListeners newAppResponseListeners() {
		return EzyClientAppResponseListenersImpl.builder().build();
	}
	
	protected Set<EzyConstant> defaultUnloggableCommands() {
		return Sets.newHashSet(EzyClientCommand.PING, EzyClientCommand.PONG);
	}
}

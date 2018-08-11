package com.tvd12.ezyfoxserver.nio;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;

public abstract class EzyAbstractSocketServerBootstrap implements EzyStartable, EzyDestroyable {

	protected EzyServerContext serverContext;
	protected EzyHandlerGroupManager handlerGroupManager;
	protected EzySessionTicketsQueue sessionTicketsQueue;
	protected EzySocketEventLoopHandler writingLoopHandler;

	public EzyAbstractSocketServerBootstrap(Builder<?,?> builder) {
		this.serverContext = builder.serverContext;
		this.handlerGroupManager = builder.handlerGroupManager;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
	}
	
	@Override
	public void destroy() {
		processWithLogException(writingLoopHandler::destroy);
	}
	
	protected final EzySessionManagementSetting getSessionSetting() {
		return getServerSettings().getSessionManagement();
	}
	
	protected final EzySettings getServerSettings() {
		return serverContext.getServer().getSettings();
	}
	
	protected final EzyNioSessionManager getSessionManager() {
		return (EzyNioSessionManager) 
				serverContext.getServer().getSessionManager();
	}
	
	@SuppressWarnings("unchecked")
	public static abstract class Builder<B, T extends EzyAbstractSocketServerBootstrap> 
			implements EzyBuilder<T> {

		protected EzyServerContext serverContext;
		protected EzyHandlerGroupManager handlerGroupManager;
		protected EzySessionTicketsQueue sessionTicketsQueue;
		
		public B serverContext(EzyServerContext context) {
			this.serverContext = context;
			return (B) this;
		}
		
		public B handlerGroupManager(EzyHandlerGroupManager manager) {
			this.handlerGroupManager = manager;
			return (B) this;
		}
		
		public B sessionTicketsQueue(EzySessionTicketsQueue queue) {
			this.sessionTicketsQueue = queue;
			return (B) this;
		}
	}
}

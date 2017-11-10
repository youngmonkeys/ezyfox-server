package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzySocketServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerBootstrap;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;

import lombok.Setter;

public class EzyNioServerBootstrap extends EzyServerBootstrap {

	private EzySocketServerBootstrap socketServerBootstrap;
	private EzyWebSocketServerBootstrap websocketServerBootstrap;
	
	@Setter
	private EzyHandlerGroupManager handlerGroupManager;
	@Setter
	private EzySessionTicketsQueue socketSessionTicketsQueue;
	@Setter
	private EzySessionTicketsQueue websocketSessionTicketsQueue;
	
	@Override
	protected void startOtherBootstraps(Runnable callback) throws Exception {
		startSocketServerBootstrap();
		startWebSocketServerBootstrap();
		callback.run();
	}
	
	private void startSocketServerBootstrap() throws Exception {
		getLogger().debug("starting tcp socket server bootstrap ....");
		socketServerBootstrap = newSocketServerBootstrap();
		socketServerBootstrap.start();
		getLogger().debug("tcp socket server bootstrap has started");
	}
	
	protected void startWebSocketServerBootstrap() throws Exception {
		getLogger().debug("starting websocket server bootstrap ....");
		websocketServerBootstrap = newWebSocketServerBootstrap();
		websocketServerBootstrap.start();
		getLogger().debug("websockt server bootstrap has started");
	}
	
	private EzySocketServerBootstrap newSocketServerBootstrap() {
		return EzySocketServerBootstrap.builder()
				.serverContext(context)
				.handlerGroupManager(handlerGroupManager)
				.sessionTicketsQueue(socketSessionTicketsQueue)
				.build();
	}
	
	private EzyWebSocketServerBootstrap newWebSocketServerBootstrap() {
		return EzyWebSocketServerBootstrap.builder()
				.serverContext(context)
				.handlerGroupManager(handlerGroupManager)
				.sessionTicketsQueue(websocketSessionTicketsQueue)
				.build();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		socketServerBootstrap.destroy();
		websocketServerBootstrap.destroy();
	}
	
}

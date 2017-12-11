package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzySocketServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerBootstrap;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;

import lombok.Setter;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.*;

import javax.net.ssl.SSLContext;


public class EzyNioServerBootstrap extends EzyServerBootstrap {

	private EzySocketServerBootstrap socketServerBootstrap;
	private EzyWebSocketServerBootstrap websocketServerBootstrap;
	
	@Setter
	private SSLContext sslContext;
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
		EzySocketSetting socketSetting = getSocketSetting();
		if(!socketSetting.isActive()) return;
		getLogger().debug("starting tcp socket server bootstrap ....");
		socketServerBootstrap = newSocketServerBootstrap();
		socketServerBootstrap.start();
		getLogger().debug("tcp socket server bootstrap has started");
	}
	
	protected void startWebSocketServerBootstrap() throws Exception {
		EzyWebSocketSetting socketSetting = getWebSocketSetting();
		if(!socketSetting.isActive()) return;
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
				.sslContext(sslContext)
				.handlerGroupManager(handlerGroupManager)
				.sessionTicketsQueue(websocketSessionTicketsQueue)
				.build();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(socketServerBootstrap != null)
			processWithLogException(socketServerBootstrap::destroy);
		if(websocketServerBootstrap != null)
			processWithLogException(websocketServerBootstrap::destroy);
	}
	
}

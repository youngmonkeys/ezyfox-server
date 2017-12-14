package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;

import lombok.Setter;

@Setter
public class EzyNioConnectionAcceptor extends EzySocketAbstractEventHandler {

	private EzyNioAcceptableConnectionsHandler acceptableConnectionsHandler;
	
	@Override
	public void handleEvent() {
		try {
			acceptableConnectionsHandler.handleAcceptableConnections();
			Thread.sleep(5);
		}
		catch(Exception e) {
			getLogger().info("I/O error at connection-acceptor", e);
		}
	}

	@Override
	public void destroy() {
	}

}

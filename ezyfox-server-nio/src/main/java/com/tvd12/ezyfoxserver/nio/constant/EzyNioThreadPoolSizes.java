package com.tvd12.ezyfoxserver.nio.constant;

public final class EzyNioThreadPoolSizes {

	public static final int STATISTICS		= 1;
	public static final int CODEC			= 3;
	public static final int SYSTEM_REQUEST_HANDLER		= 8;
	public static final int EXTENSION_REQUEST_HANDLER		= 8;
	public static final int SOCKET_DISCONNECTION_HANDLER = 3;
	public static final int SOCKET_READER		= 1;
	public static final int SOCKET_WRITER		= 3;
	public static final int SOCKET_ACCEPTOR		= 1;
	public static final int WEBSOCKET_WRITER		= 3;
	
	private EzyNioThreadPoolSizes() {
	}
	
}

package com.tvd12.ezyfoxserver.netty.constant;

public final class EzyNettyThreadPoolSizes {

	public static final int STATISTICS		= 1;
	public static final int SYSTEM_REQUEST_HANDLER		= 8;
	public static final int EXTENSION_REQUEST_HANDLER		= 8;
	public static final int PARENT_EVENT_LOOP_GROUP		= 0;
	public static final int SOCKET_EVENT_LOOP_GROUP		= 0;
	public static final int SOCKET_WRITER		= 3;
	public static final int WEBSOCKET_WRITER		= 3;
	
	private EzyNettyThreadPoolSizes() {
	}
	
}

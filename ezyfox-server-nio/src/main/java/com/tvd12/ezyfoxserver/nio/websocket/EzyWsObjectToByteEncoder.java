package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.nio.handler.EzyNioObjectToByteEncoder;

public interface EzyWsObjectToByteEncoder extends EzyNioObjectToByteEncoder {

	<T> T encode(Object msg, Class<T> outType) throws Exception;
	
}

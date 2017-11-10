package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.nio.handler.EzyNioDataEncoder;

public interface EzyWsDataEncoder extends EzyNioDataEncoder {

	<T> T encode(Object data, Class<T> outType) throws Exception;
	
}

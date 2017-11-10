package com.tvd12.ezyfoxserver.nio.websocket;

import java.nio.ByteBuffer;

public interface EzyWsByteToObjectDecoder {
	
	Object decode(String bytes) throws Exception;

	Object decode(ByteBuffer bytes) throws Exception;
	
}

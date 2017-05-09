package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;

public interface EzyNioDataEncoder {

	ByteBuffer encode(Object data) throws Exception;
	
}

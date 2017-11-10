package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;

public interface EzyNioObjectToByteEncoder {

	ByteBuffer encode(Object msg) throws Exception;
	
}

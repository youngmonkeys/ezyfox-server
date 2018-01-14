package com.tvd12.ezyfoxserver.nio.handler;

public interface EzyNioObjectToByteEncoder {

	byte[] encode(Object msg) throws Exception;
	
}

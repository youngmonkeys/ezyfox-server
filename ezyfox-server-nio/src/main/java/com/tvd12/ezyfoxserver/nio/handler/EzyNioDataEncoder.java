package com.tvd12.ezyfoxserver.nio.handler;

public interface EzyNioDataEncoder {

	byte[] encode(Object data) throws Exception;
	
}

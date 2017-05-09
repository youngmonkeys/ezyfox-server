package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;
import java.util.Queue;

import com.tvd12.ezyfoxserver.codec.EzyMessage;

public interface EzyNioByteToObjectDecoder {

	Object decode(EzyMessage message) throws Exception;
	
	void decode(ByteBuffer bytes, Queue<EzyMessage> queue) throws Exception;
	
}

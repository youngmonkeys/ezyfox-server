package com.tvd12.ezyfoxserver.nio.codec;

import com.tvd12.ezyfox.codec.EzyMessageByTypeSerializer;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsObjectToByteEncoder;

public class JacksonMessageToByteEncoder implements EzyWsObjectToByteEncoder {

	protected final EzyMessageByTypeSerializer serializer;
	
	public JacksonMessageToByteEncoder(EzyMessageByTypeSerializer serializer) {
		this.serializer = serializer;
	}
	
	@Override
	public byte[] encode(Object msg) throws Exception {
		return serializer.serialize(msg);
	}
	
	@Override
	public <T> T encode(Object msg, Class<T> outType) throws Exception {
		return serializer.serialize(msg, outType);
	}
	
}

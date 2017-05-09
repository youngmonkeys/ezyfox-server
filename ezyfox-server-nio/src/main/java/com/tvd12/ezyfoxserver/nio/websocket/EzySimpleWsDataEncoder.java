package com.tvd12.ezyfoxserver.nio.websocket;

import java.nio.ByteBuffer;

public class EzySimpleWsDataEncoder implements EzyWsDataEncoder {

	private EzyWsObjectToByteEncoder encoder;
	
	public EzySimpleWsDataEncoder(EzyWsObjectToByteEncoder encoder) {
		this.encoder = encoder;
	}
	
	@Override
	public ByteBuffer encode(Object data) throws Exception {
		return encoder.encode(data);
	}
	
	@Override
	public <T> T encode(Object data, Class<T> outType) throws Exception {
		return encoder.encode(data, outType);
	}

}

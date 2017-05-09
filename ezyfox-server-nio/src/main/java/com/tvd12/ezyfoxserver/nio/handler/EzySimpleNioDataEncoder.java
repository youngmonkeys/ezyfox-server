package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;

public class EzySimpleNioDataEncoder implements EzyNioDataEncoder {

	private EzyNioObjectToByteEncoder encoder;
	
	public EzySimpleNioDataEncoder(EzyNioObjectToByteEncoder encoder) {
		this.encoder = encoder;
	}
	
	@Override
	public ByteBuffer encode(Object data) throws Exception {
		return encoder.encode(data);
	}

}

package com.tvd12.ezyfoxserver.nio.handler;

public class EzySimpleNioDataEncoder implements EzyNioDataEncoder {

	private EzyNioObjectToByteEncoder encoder;
	
	public EzySimpleNioDataEncoder(EzyNioObjectToByteEncoder encoder) {
		this.encoder = encoder;
	}
	
	@Override
	public byte[] encode(Object data) throws Exception {
		return encoder.encode(data);
	}

}

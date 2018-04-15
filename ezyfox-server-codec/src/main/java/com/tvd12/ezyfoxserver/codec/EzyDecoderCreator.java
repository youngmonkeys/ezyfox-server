package com.tvd12.ezyfoxserver.codec;

public interface EzyDecoderCreator {

	Object newDecoder(int maxRequestSize);
	
}

package com.tvd12.ezyfoxserver.codec;

public interface EzyCodecCreator {

	Object newEncoder();
	
	Object newDecoder(int maxRequestSize);
	
}

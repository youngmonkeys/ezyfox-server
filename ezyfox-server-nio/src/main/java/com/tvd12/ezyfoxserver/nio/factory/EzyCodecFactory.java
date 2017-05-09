package com.tvd12.ezyfoxserver.nio.factory;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public interface EzyCodecFactory {

	Object newDecoder(EzyConnectionType type);
	
	Object newEncoder(EzyConnectionType type);
	
}

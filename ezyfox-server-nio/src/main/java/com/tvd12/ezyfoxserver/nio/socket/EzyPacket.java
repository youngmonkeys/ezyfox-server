package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;

public interface EzyPacket {

	Object getData();
	
	EzyTransportType getType();
	
}

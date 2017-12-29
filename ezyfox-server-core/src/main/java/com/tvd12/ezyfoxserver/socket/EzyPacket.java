package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.util.EzyReleasable;

public interface EzyPacket extends EzyReleasable {

	Object getData();
	
	boolean isReleased();
	
	boolean isFragmented();
	
	EzyTransportType getType();
	
	void setFragment(Object fragment);
	
	int getSize();
	
}

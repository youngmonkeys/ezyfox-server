package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyReleasable;

public interface EzyPacket extends EzyReleasable {

	Object getData();
	
	boolean isReleased();
	
	boolean isFragmented();
	
	void setFragment(Object fragment);
	
	EzyConstant getTransportType();
	
	int getSize();
	
}

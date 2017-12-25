package com.tvd12.ezyfoxserver.socket;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.util.EzyReleasable;

public interface EzyPacket extends EzyReleasable {

	Object getData();
	
	boolean isReleased();
	
	boolean isFragmented();
	
	EzyTransportType getType();
	
	ByteBuffer getFragment();
	
	void setFragment(ByteBuffer fragment);
	
}

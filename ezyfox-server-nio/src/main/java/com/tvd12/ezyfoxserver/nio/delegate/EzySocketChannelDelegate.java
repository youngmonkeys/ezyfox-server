package com.tvd12.ezyfoxserver.nio.delegate;

import com.tvd12.ezyfoxserver.nio.entity.EzyChannel;

public interface EzySocketChannelDelegate {

	void onChannelInactivated(EzyChannel channel);
	
}

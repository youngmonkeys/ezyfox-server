package com.tvd12.ezyfoxserver.nio.delegate;

import com.tvd12.ezyfoxserver.socket.EzyChannel;

public interface EzySocketChannelDelegate {

	void onChannelInactivated(EzyChannel channel);
	
}

package com.tvd12.ezyfoxserver.creator;

import io.netty.channel.ChannelHandler;

public interface EzyDataHandlerCreator {

	ChannelHandler newHandler();
	
}

package com.tvd12.ezyfoxserver.creator;

import io.netty.channel.ChannelHandlerAdapter;

public interface EzyDataHandlerCreator {

	ChannelHandlerAdapter newHandler();
	
}

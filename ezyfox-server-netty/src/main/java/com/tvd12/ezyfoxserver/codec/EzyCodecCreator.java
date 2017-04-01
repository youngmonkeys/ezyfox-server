package com.tvd12.ezyfoxserver.codec;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public interface EzyCodecCreator {

	ChannelOutboundHandler newEncoder();
	
	ChannelInboundHandlerAdapter newDecoder();
	
}

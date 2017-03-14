package com.tvd12.ezyfoxserver.codec;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.CombinedChannelDuplexHandler;

public class EzyCombinedCodec 
	extends CombinedChannelDuplexHandler<ChannelInboundHandlerAdapter, ChannelOutboundHandler> {
	
	public EzyCombinedCodec(ChannelInboundHandlerAdapter decoder, ChannelOutboundHandler encoder) {
		super(decoder, encoder);
	}
}

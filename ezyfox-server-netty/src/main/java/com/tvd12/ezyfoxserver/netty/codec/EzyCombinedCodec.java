package com.tvd12.ezyfoxserver.netty.codec;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.CombinedChannelDuplexHandler;

public class EzyCombinedCodec 
	extends CombinedChannelDuplexHandler<ChannelInboundHandler, ChannelOutboundHandler> {
	
	public EzyCombinedCodec(ChannelInboundHandler decoder, ChannelOutboundHandler encoder) {
		super(decoder, encoder);
	}
}

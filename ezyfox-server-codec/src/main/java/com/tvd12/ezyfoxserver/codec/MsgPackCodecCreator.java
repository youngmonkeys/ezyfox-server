package com.tvd12.ezyfoxserver.codec;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class MsgPackCodecCreator implements EzyCodecCreator {

	@Override
	public ChannelOutboundHandler newEncoder() {
		return new MsgPackMessageToByteEncoder();
	}

	@Override
	public ChannelInboundHandlerAdapter newDecoder() {
		return new MsgPackByteToMessageDecoder();
	}

}

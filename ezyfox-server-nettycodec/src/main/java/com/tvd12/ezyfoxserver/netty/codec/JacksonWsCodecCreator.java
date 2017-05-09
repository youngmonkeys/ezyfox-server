package com.tvd12.ezyfoxserver.netty.codec;

import io.netty.channel.ChannelOutboundHandler;

public class JacksonWsCodecCreator extends JacksonCodecCreator {

	public JacksonWsCodecCreator() {
		super();
	}
	
	@Override
	public ChannelOutboundHandler newEncoder() {
		return new JacksonMessageToTextWsFrameEncoder(serializer);
	}

}

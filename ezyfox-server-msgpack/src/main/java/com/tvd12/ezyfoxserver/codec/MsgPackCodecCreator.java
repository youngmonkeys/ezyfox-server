package com.tvd12.ezyfoxserver.codec;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class MsgPackCodecCreator implements EzyCodecCreator {

	protected EzyMessageToBytes messageToBytes;
	protected EzyObjectToMessage objectToMessage;
	protected EzyMessageDeserializer deserializer;
	
	{
		deserializer = new MsgPackSimpleDeserializer();
		messageToBytes = EzySimpleMessageToBytes.builder().build();
		objectToMessage = MsgPackObjectToMessage.builder().build();
	}
	
	@Override
	public ChannelInboundHandlerAdapter newDecoder() {
		return new MsgPackByteToMessageDecoder(deserializer);
	}
	
	@Override
	public ChannelOutboundHandler newEncoder() {
		return new MsgPackMessageToByteEncoder(messageToBytes, objectToMessage);
	}

}

package com.tvd12.ezyfoxserver.netty.codec;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageToBytes;
import com.tvd12.ezyfoxserver.codec.EzyObjectToMessage;
import com.tvd12.ezyfoxserver.codec.MsgPackObjectToMessage;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.netty.codec.EzySimpleMessageToBytes;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class MsgPackCodecCreator implements EzyCodecCreator {

	protected final EzyMessageToBytes messageToBytes 
			= EzySimpleMessageToBytes.builder().build();
	protected final EzyObjectToMessage objectToMessage 
			= MsgPackObjectToMessage.builder().build();
	protected final EzyMessageDeserializer deserializer 
			= new MsgPackSimpleDeserializer();
	
	@Override
	public ChannelInboundHandlerAdapter newDecoder(int maxRequestSize) {
		return new MsgPackByteToMessageDecoder(deserializer, maxRequestSize);
	}
	
	@Override
	public ChannelOutboundHandler newEncoder() {
		return new MsgPackMessageToByteEncoder(messageToBytes, objectToMessage);
	}

}

package com.tvd12.ezyfoxserver.netty.codec;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfox.codec.EzyMessageToBytes;
import com.tvd12.ezyfox.codec.EzyObjectToMessage;
import com.tvd12.ezyfox.codec.MsgPackObjectToMessage;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
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

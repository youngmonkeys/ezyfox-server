package com.tvd12.ezyfoxserver.codec;

import org.msgpack.MessagePack;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class MsgPackCodecCreator implements EzyCodecCreator {

	private MessagePack msgPack;
	
	public MsgPackCodecCreator() {
		this.msgPack = new MessagePack();
		this.msgPack.register(EzyObject.class, new MsgPackObjectTemplate());
		this.msgPack.register(EzyArray.class, new MsgPackArrayTemplate());
	}
	
	@Override
	public ChannelOutboundHandler newEncoder() {
		return new MsgPackMessageToByteEncoder();
	}

	@Override
	public ChannelInboundHandlerAdapter newDecoder() {
		return new MsgPackByteToMessageDecoder();
	}

}

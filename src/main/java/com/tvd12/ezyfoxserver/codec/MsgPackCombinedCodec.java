package com.tvd12.ezyfoxserver.codec;

import org.msgpack.MessagePack;

import io.netty.channel.CombinedChannelDuplexHandler;

public class MsgPackCombinedCodec
		extends CombinedChannelDuplexHandler<MsgPackByteToMessageDecoder, MsgPackMessageToByteEncoder> {

	public MsgPackCombinedCodec() {
		this(new MessagePack());
	}
	
	public MsgPackCombinedCodec(MessagePack msgPack) {
		super(new MsgPackByteToMessageDecoder(msgPack), new MsgPackMessageToByteEncoder(msgPack));
	}
	
}

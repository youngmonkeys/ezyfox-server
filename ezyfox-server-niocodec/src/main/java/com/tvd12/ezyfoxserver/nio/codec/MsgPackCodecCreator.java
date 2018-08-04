package com.tvd12.ezyfoxserver.nio.codec;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfox.codec.EzyMessageToBytes;
import com.tvd12.ezyfox.codec.EzyObjectToMessage;
import com.tvd12.ezyfox.codec.MsgPackObjectToMessage;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioByteToObjectDecoder;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioObjectToByteEncoder;

public class MsgPackCodecCreator implements EzyCodecCreator {

	protected final EzyMessageToBytes messageToBytes 
			= EzySimpleMessageToBytes.builder().build();
	protected final EzyObjectToMessage objectToMessage 
			= MsgPackObjectToMessage.builder().build();
	protected final EzyMessageDeserializer deserializer 
			= new MsgPackSimpleDeserializer();
	
	@Override
	public EzyNioByteToObjectDecoder newDecoder(int maxRequestSize) {
		return new MsgPackByteToObjectDecoder(deserializer, maxRequestSize);
	}
	
	@Override
	public EzyNioObjectToByteEncoder newEncoder() {
		return new MsgPackObjectToByteEncoder(messageToBytes, objectToMessage);
	}

}

package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageToBytes;
import com.tvd12.ezyfoxserver.codec.EzyObjectToMessage;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioObjectToByteEncoder;

public class MsgPackObjectToByteEncoder implements EzyNioObjectToByteEncoder {

	protected final EzyMessageToBytes messageToBytes;
	protected final EzyObjectToMessage objectToMessage;
	
	public MsgPackObjectToByteEncoder(
			EzyMessageToBytes messageToBytes,
			EzyObjectToMessage objectToMessage) {
		this.messageToBytes = messageToBytes;
		this.objectToMessage = objectToMessage;
	}
	
	@Override
	public ByteBuffer encode(Object msg) throws Exception {
		return convertObjectToBytes(msg);
	}
	
	protected ByteBuffer convertObjectToBytes(Object object) {
		return convertMessageToBytes(convertObjectToMessage(object));
	}
	
	protected EzyMessage convertObjectToMessage(Object object) {
		return objectToMessage.convert(object);
	}
	
	protected ByteBuffer convertMessageToBytes(EzyMessage message) {
		return messageToBytes.convert(message);
	}
	
}

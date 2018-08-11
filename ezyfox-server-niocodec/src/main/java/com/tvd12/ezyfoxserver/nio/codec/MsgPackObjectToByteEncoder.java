package com.tvd12.ezyfoxserver.nio.codec;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageToBytes;
import com.tvd12.ezyfox.codec.EzyObjectToMessage;
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
	public byte[] encode(Object msg) throws Exception {
		return convertObjectToBytes(msg);
	}
	
	protected byte[] convertObjectToBytes(Object object) {
		return convertMessageToBytes(convertObjectToMessage(object));
	}
	
	protected EzyMessage convertObjectToMessage(Object object) {
		return objectToMessage.convert(object);
	}
	
	protected byte[] convertMessageToBytes(EzyMessage message) {
		return messageToBytes.convert(message);
	}
	
}

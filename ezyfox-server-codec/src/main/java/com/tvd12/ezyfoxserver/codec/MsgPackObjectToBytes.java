package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;

import org.msgpack.MessagePack;

import com.tvd12.ezyfoxserver.codec.EzyObjectToBytes;

import lombok.Builder;

@Builder
public class MsgPackObjectToBytes implements EzyObjectToBytes {

	private MessagePack msgPack;
	
	@Override
	public byte[] convert(Object object) {
		try {
			return msgPack.write(object);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}

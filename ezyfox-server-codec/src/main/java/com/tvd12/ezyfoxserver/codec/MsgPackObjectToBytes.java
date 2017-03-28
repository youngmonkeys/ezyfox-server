package com.tvd12.ezyfoxserver.codec;

import lombok.Builder;

@Builder
public class MsgPackObjectToBytes implements EzyObjectToBytes {

	private EzyMessageSerializer serializer;
	
	@Override
	public byte[] convert(Object object) {
		try {
			return serializer.serialize(object);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}

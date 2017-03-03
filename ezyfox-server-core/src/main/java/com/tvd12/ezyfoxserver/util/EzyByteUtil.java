package com.tvd12.ezyfoxserver.util;

import java.nio.ByteBuffer;

public abstract class EzyByteUtil {

	protected EzyByteUtil() {
	}
	
	public static byte[] copy(ByteBuffer buffer, int size) {
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		return bytes;
	}
	
}

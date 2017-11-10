package com.tvd12.ezyfoxserver.io;

import java.nio.ByteBuffer;

public final class EzyByteBuffers {
	
	private EzyByteBuffers() {
	}
	
	public static byte[] getBytes(ByteBuffer buffer) {
		if(!buffer.hasRemaining())
			buffer.flip();
		return getBytes(buffer, buffer.remaining());
	}
	
	public static byte[] getBytes(ByteBuffer buffer, int size) {
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		return bytes;
	}

}

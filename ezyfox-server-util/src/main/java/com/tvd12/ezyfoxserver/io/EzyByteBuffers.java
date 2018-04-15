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

	public static ByteBuffer merge(ByteBuffer[] buffers) {
		ByteBuffer answer = ByteBuffer.allocate(totalBytes(buffers));
		for(ByteBuffer buffer : buffers) {
			answer.put(buffer);
		}
		answer.flip();
		return answer;
	}
	
	public static int totalBytes(ByteBuffer[] buffers) {
		int size = 0;
		for(ByteBuffer buffer : buffers)
			size += buffer.remaining();
		return size;
	}
	
	public static byte[] merge2bytes(byte first, byte[] other) {
		ByteBuffer buffer = ByteBuffer.allocate(other.length + 1);
		buffer.put(first);
		buffer.put(other);
		byte[] bytes = new byte[buffer.position()];
		buffer.flip();
		buffer.get(bytes);
		return bytes;
	}
}

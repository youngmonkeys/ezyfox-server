package com.tvd12.ezyfoxserver.util;

import java.nio.ByteBuffer;

public abstract class EzyBytes {

	protected EzyBytes() {
	}
	
	public static byte[] copy(ByteBuffer buffer, int size) {
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		return bytes;
	}
	
	public static byte[] copy(byte[][] bytess) {
		int index = 0;
		byte[] answer = new byte[totalBytes(bytess)];
		for(byte[] bytes : bytess) {
			EzyArrays.copy(bytes, answer, index);
			index += bytes.length;
		}
		return answer;
	}
	
	public static int totalBytes(byte[][] bytess) {
		int size = 0;
		for(byte[] bytes : bytess)
			size += bytes.length;
		return size;
	}
	
}

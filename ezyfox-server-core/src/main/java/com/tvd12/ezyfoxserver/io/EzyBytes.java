package com.tvd12.ezyfoxserver.io;

import java.nio.ByteBuffer;

public abstract class EzyBytes {

	protected EzyBytes() {
	}
	
	public static byte[] copy(ByteBuffer buffer, int size) {
		return EzyByteBuffers.getBytes(buffer, size);
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
	
	public static byte[] getBytes(long value, int size) {
		byte[] bytes = new byte[size];
		for(int i = 0 ; i < size ; i++)
			bytes[i] = (byte)(value >> ((size - i - 1) * 8));
		return bytes;
	}
	
	public static byte[] getBytes(int first, int value, int size) {
		return getBytes(first, (long)value, size);
	}
	
	public static byte[] getBytes(int first, long value, int size) {
		byte[] bytes = getBytes((byte)first, value, size);
		System.out.println("value = " + value + ", after = " + EzyLongs.bin2long(bytes));
		return bytes;
	}
	
	public static byte[] getBytes(byte first, long value, int size) {
		return EzyArrays.merge(first, getBytes(value, size));
	}
	
}

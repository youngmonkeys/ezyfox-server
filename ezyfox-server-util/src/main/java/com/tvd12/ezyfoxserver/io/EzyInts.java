package com.tvd12.ezyfoxserver.io;

import java.nio.ByteBuffer;

public final class EzyInts {

	private EzyInts() {
	}
	
	public static int bin2int(int length) {
		return EzyMath.bin2int(length);
	}
	
	public static int bin2int(byte[] bytes) {
		return EzyMath.bin2int(bytes);
	}
	
	public static int bin2uint(byte[] bytes) {
		return EzyMath.bin2uint(bytes);
	}
	
	public static int bin2int(ByteBuffer buffer) {
		return bin2int(buffer, 4);
	}
	
	public static int bin2uint(ByteBuffer buffer) {
		return bin2uint(buffer, 4);
	}
	
	public static int bin2int(ByteBuffer buffer, int size) {
		return bin2int(EzyBytes.copy(buffer, size));
	}
	
	public static int bin2uint(ByteBuffer buffer, int size) {
		return bin2uint(EzyBytes.copy(buffer, size));
	}
	
}

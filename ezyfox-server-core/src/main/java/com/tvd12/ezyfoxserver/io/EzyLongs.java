package com.tvd12.ezyfoxserver.io;

import java.nio.ByteBuffer;

public abstract class EzyLongs {

	private EzyLongs() {
	}
	
	public static long bin2long(int length) {
		return EzyMath.bin2long(length);
	}
	
	public static long bin2long(byte[] bytes) {
		return EzyMath.bin2ulong(bytes);
	}
	
	public static long bin2long(ByteBuffer buffer, int size) {
		return bin2long(EzyBytes.copy(buffer, size));
	}
	
}

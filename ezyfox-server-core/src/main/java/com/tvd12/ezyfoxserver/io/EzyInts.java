package com.tvd12.ezyfoxserver.io;

import java.nio.ByteBuffer;

public abstract class EzyInts {

	private EzyInts() {
	}
	
	public static int bin2int(int length) {
		return EzyMath.bin2int(length);
	}
	
	public static int bin2int(byte[] bytes) {
		return EzyMath.bin2int(bytes);
	}
	
	public static int bin2int(ByteBuffer buffer, int size) {
		return bin2int(EzyBytes.copy(buffer, size));
	}
	
}

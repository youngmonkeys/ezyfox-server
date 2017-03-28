package com.tvd12.ezyfoxserver.io;

import io.netty.buffer.ByteBuf;

public abstract class EzyByteBufs {

	private EzyByteBufs() {
	}
	
	public static byte[] getBytes(ByteBuf buf) {
		return getBytes(buf, buf.readableBytes());
	}
	
	public static byte[] getBytes(ByteBuf buf, int size) {
		byte[] bytes = new byte[size];
		buf.readBytes(bytes);
		return bytes;
	}
	
}

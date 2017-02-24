package com.tvd12.ezyfoxserver.codec;

import io.netty.buffer.ByteBuf;

public class EzyMessageReader {
	
	private int size;
	private byte[] content;
	
	public EzyMessageReader() {
		clear();
	}
	
	public boolean readSize(ByteBuf buf) {
		if(buf.readableBytes() <= 4)
			return false;
		this.size = buf.readInt();
		return true;
	}
	
	public boolean readContent(ByteBuf buf) {
		if(buf.readableBytes() < size)
			return false;
		this.content = new byte[size];
		buf.readBytes(content);
		return true;
	}
	
	public void clear() {
		this.size = 0;
		this.content = new byte[0];
	}
	
	public EzyMessage get() {
		EzyMessage answer = new EzyMessage();
		answer.setSize(size);
		answer.setContent(content);
		return answer;
	}
	
}

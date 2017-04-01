package com.tvd12.ezyfoxserver.codec;

import io.netty.buffer.ByteBuf;

public class EzyMessageReader {
	
	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	
	public EzyMessageReader() {
		clear();
	}
	
	public boolean readHeader(ByteBuf buf) {
		if(buf.readableBytes() < getHeaderLength())
			return false;
		readHeader(buf.readByte());
		return true;
	}
	
	public boolean readSize(ByteBuf buf) {
		if(buf.readableBytes() < getSizeLength())
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
		return EzyMessageBuilder.messageBuilder()
				.header(header)
				.size(size)
				.content(content)
				.build();
	}
	
	private void readHeader(byte header) {
		this.header = new EzyMessageHeaderReader().read(header);
	}
	
	protected int getSizeLength() {
		return header.isBigSize() ? 4 : 2;
	}
	
	protected int getHeaderLength() {
		return 1;
	}
}

class EzyMessageHeaderReader {
	
	protected boolean bigSize;
	protected boolean encrypted;
	protected boolean compressed;
	
	protected boolean readBigSize(byte header) {
		return (header & 1) > 0;
	}
	
	protected boolean readEncrypted(byte header) {
		return (header & (1 << 1)) > 0;
	}
	
	protected boolean readCompressed(byte header) {
		return (header & (2 << 1)) > 0;
	}
	
	public EzyMessageHeader read(byte header) {
		return  EzyMessageBuilder
				.headerBuilder()
				.bigSize(readBigSize(header))
				.encrypted(readEncrypted(header))
				.compressed(readCompressed(header))
				.build();
	}
}
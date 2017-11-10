package com.tvd12.ezyfoxserver.netty.codec;

import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeader;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeaderBuilder;
import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfoxserver.io.EzyInts;
import com.tvd12.ezyfoxserver.netty.io.EzyByteBufs;

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
	
	public boolean readSize(ByteBuf buf, int maxSize) {
		if(buf.readableBytes() < getSizeLength())
			return false;
		this.size = EzyInts.bin2uint(EzyByteBufs.getBytes(buf, getSizeLength()));
		if(size > maxSize)
			throw new EzyMaxRequestSizeException(size, maxSize);
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
		return EzyMessageBuilder.newInstance()
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
		return (header & (1 << 2)) > 0;
	}
	
	protected boolean readText(byte header) {
		return (header & (1 << 3)) > 0;
	}
	
	public EzyMessageHeader read(byte header) {
		return EzyMessageHeaderBuilder.newInstance()
				.bigSize(readBigSize(header))
				.encrypted(readEncrypted(header))
				.compressed(readCompressed(header))
				.text(readText(header))
				.build();
	}
}
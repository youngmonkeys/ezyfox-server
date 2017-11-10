package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeader;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeaderBuilder;
import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyInts;

public class EzyMessageReader {
	
	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	
	public EzyMessageReader() {
		clear();
	}
	
	public boolean readHeader(ByteBuffer buf) {
		if(buf.remaining() < getHeaderLength())
			return false;
		readHeader(buf.get());
		return true;
	}
	
	public boolean readSize(ByteBuffer buf, int maxSize) {
		if(buf.remaining() < getSizeLength())
			return false;
		this.size = EzyInts.bin2uint(EzyBytes.copy(buf, getSizeLength()));
		if(size > maxSize)
			throw new EzyMaxRequestSizeException(size, maxSize);
		return true;
	}
	
	public boolean readContent(ByteBuffer buf) {
		if(buf.remaining() < size)
			return false;
		this.content = new byte[size];
		buf.get(content);
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
		return (header & 1 << 0) > 0;
	}
	
	protected boolean readEncrypted(byte header) {
		return (header & (1 << 1)) > 0;
	}
	
	protected boolean readCompressed(byte header) {
		return (header & (2 << 1)) > 0;
	}
	
	protected boolean readText(byte header) {
		return (header & (3 << 1)) > 0;
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
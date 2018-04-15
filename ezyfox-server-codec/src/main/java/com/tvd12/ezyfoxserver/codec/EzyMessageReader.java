package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;

public abstract class EzyMessageReader<B> {
	
	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	private EzyMessageHeaderReader headerReader = newMessageHeaderReader(); 
	
	public EzyMessageReader() {
		clear();
	}

	protected abstract int remaining(B buffer);
	protected abstract byte readByte(B buffer);
	protected abstract int readMessgeSize(B buffer);
	protected abstract void readMessageContent(B buffer, byte[] content);
	
	protected EzyMessageHeaderReader newMessageHeaderReader() {
		return new EzyMessageHeaderReader();
	}
	
	public boolean readHeader(B buffer) {
		int remaining = remaining(buffer);
		if(remaining < getHeaderLength())
			return false;
		byte headerByte = readByte(buffer);
		readHeader(headerByte);
		return true;
	}
	
	public boolean readSize(B buffer, int maxSize) {
		int remaining = remaining(buffer);
		if(remaining < getSizeLength())
			return false;
		this.size = readMessgeSize(buffer);
		if(size > maxSize)
			throw new EzyMaxRequestSizeException(size, maxSize);
		return true;
	}
	
	public boolean readContent(B buffer) {
		int remaining = remaining(buffer);
		if(remaining < size)
			return false;
		this.content = new byte[size];
		readMessageContent(buffer, content);
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
		this.header = headerReader.read(header);
	}
	
	protected int getSizeLength() {
		return header.isBigSize() ? 4 : 2;
	}
	
	protected int getHeaderLength() {
		return 1;
	}
}

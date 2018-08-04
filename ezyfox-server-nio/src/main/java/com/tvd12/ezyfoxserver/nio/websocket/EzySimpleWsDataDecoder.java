package com.tvd12.ezyfoxserver.nio.websocket;

import java.nio.ByteBuffer;

import com.tvd12.ezyfox.callback.EzyCallback;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractDataDecoder;

public class EzySimpleWsDataDecoder
		extends EzyAbstractDataDecoder<EzyWsByteToObjectDecoder>
		implements EzyWsDataDecoder {

	public EzySimpleWsDataDecoder(EzyWsByteToObjectDecoder decoder) {
		super(decoder);
		this.buffer = ByteBuffer.allocate(32768);
	}
	
	@Override
	public void decode(String bytes, EzyCallback<Object> callback) throws Exception {
		callback.call(decoder.decode(bytes));
	}
	
	@Override
	public void decode(byte[] bytes, int offset, int len, EzyCallback<Object> callback) throws Exception {
		ByteBuffer buffer = newBuffer(bytes, offset, len);
		callback.call(decoder.decode(buffer));
	}
	
	private ByteBuffer newBuffer(byte[] bytes, int offset, int len) {
		ByteBuffer used = buffer;
		int capacity = len - offset;
		if(buffer.capacity() < capacity)
			used = ByteBuffer.allocate(capacity);
		used.clear();
		used.put(bytes, offset, len);
		used.flip();
		return used;
	}
	
}

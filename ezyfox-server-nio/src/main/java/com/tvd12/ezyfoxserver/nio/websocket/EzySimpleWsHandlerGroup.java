package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfox.callback.EzyCallback;
import com.tvd12.ezyfox.codec.EzyStringToObjectDecoder;
import com.tvd12.ezyfox.io.EzyBytes;
import com.tvd12.ezyfox.codec.EzyMessageHeaderReader;
import com.tvd12.ezyfox.codec.EzySimpleStringDataDecoder;
import com.tvd12.ezyfox.codec.EzyStringDataDecoder;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketStream;
import com.tvd12.ezyfoxserver.socket.EzySocketStream;

public class EzySimpleWsHandlerGroup
		extends EzyAbstractHandlerGroup<EzyStringDataDecoder>
		implements EzyWsHandlerGroup {

	private final EzyCallback<Object> decodeBytesCallback;
	
	public EzySimpleWsHandlerGroup(Builder builder) {
		super(builder);
		this.decodeBytesCallback = newDecodeBytesCallback();
	}
	
	protected EzyCallback<Object> newDecodeBytesCallback() {
		return new EzyCallback<Object>() {
			@Override
			public void call(Object data) {
				throw new UnsupportedOperationException("should use call(data, dataSize)");
			}
		
			@Override
			public void call(Object data, int dataSize) {
				handleReceivedData(data, dataSize);
			}
		};
	}
	
	@Override
	protected EzyStringDataDecoder newDecoder(Object decoder) {
		return new EzySimpleStringDataDecoder((EzyStringToObjectDecoder)decoder);
	}
	
	@Override
	public void fireBytesReceived(String bytes) throws Exception {
		handleReceivedBytes(bytes);
		executeAddReadBytes(bytes.length());
	}
	
	@Override
	public void fireBytesReceived(byte[] bytes, int offset, int len) throws Exception {
		handleReceivedBytes(bytes, offset, len);
		executeAddReadBytes(len - offset);
	}
	
	@SuppressWarnings("unused")
	private void executeHandleReceivedBytes(String bytes) {
		codecThreadPool.execute(() -> handleReceivedBytes(bytes));
	}
	
	@SuppressWarnings("unused")
	private void executeHandleReceivedBytes(byte[] bytes) {
		codecThreadPool.execute(() -> handleReceivedBytes(bytes));
	}
	
	private void handleReceivedBytes(String bytes) {
		try {
			decoder.decode(bytes, decodeBytesCallback);
		}
		catch(Throwable throwable) {
			fireExceptionCaught(throwable);
		}
	}
	
	private void handleReceivedBytes(byte[] bytes) {
		try {
			decoder.decode(bytes, decodeBytesCallback);
		}
		catch(Throwable throwable) {
			fireExceptionCaught(throwable);
		}
	}
	
	private void handleReceivedBytes(byte[] bytes, int offset, int len) {
		try {
			if(len <= 1) return;
			byte headerByte = bytes[offset];
			boolean isRawBytes = EzyMessageHeaderReader.readRawBytes(headerByte);
			if(isRawBytes) {
				boolean sessionStreamingEnable = session.isStreamingEnable();
				if(!streamingEnable || !sessionStreamingEnable) {
					return;
				}
				byte[] rawBytes = EzyBytes.copy(bytes, offset, len);
				EzySocketStream stream = new EzySimpleSocketStream(session, rawBytes);
				streamQueue.add(stream);
			}
			else if(len > 1) {
				int newLen = len - 1;
				int newOffset = offset + 1;
				byte[] messageBytes = EzyBytes.copy(bytes, newOffset, newLen);
				handleReceivedBytes(messageBytes);
			}
		}
		catch(Throwable throwable) {
			fireExceptionCaught(throwable);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractHandlerGroup.Builder {
		@Override
		public EzyWsHandlerGroup build() {
			return new EzySimpleWsHandlerGroup(this);
		}
	}
	
}

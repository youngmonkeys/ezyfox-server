package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.callback.EzyCallback;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;

public class EzySimpleWsHandlerGroup
		extends EzyAbstractHandlerGroup<EzyWsDataDecoder>
		implements EzyWsHandlerGroup {

	private final EzyCallback<Object> decodeBytesCallback;
	
	public EzySimpleWsHandlerGroup(Builder builder) {
		super(builder);
		this.decodeBytesCallback = this::handleReceivedData;
	}
	
	@Override
	protected EzyWsDataDecoder newDecoder(Object decoder) {
		return new EzySimpleWsDataDecoder((EzyWsByteToObjectDecoder)decoder);
	}
	
	@Override
	public void fireBytesReceived(String bytes) throws Exception {
		executeHandleReceivedBytes(bytes);
		executeAddReadBytes(bytes.length());
	}
	
	@Override
	public void fireBytesReceived(byte[] bytes, int offset, int len) throws Exception {
		executeHandleReceivedBytes(bytes, offset, len);
		executeAddReadBytes(len - offset);
	}
	
	private void executeHandleReceivedBytes(String bytes) {
		codecThreadPool.execute(() -> handleReceivedBytes(bytes));
	}
	
	private void executeHandleReceivedBytes(byte[] bytes, int offset, int len) {
		codecThreadPool.execute(() -> handleReceivedBytes(bytes, offset, len));
	}
	
	private void handleReceivedBytes(String bytes) {
		try {
			decoder.decode(bytes, decodeBytesCallback);
		}
		catch(Throwable throwable) {
			fireExceptionCaught(throwable);
		}
	}
	
	private void handleReceivedBytes(byte[] bytes, int offset, int len) {
		try {
			decoder.decode(bytes, offset, len, decodeBytesCallback);
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

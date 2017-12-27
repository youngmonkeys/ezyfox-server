package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.callback.EzyCallback;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;

public class EzySimpleWsHandlerGroup
		extends EzyAbstractHandlerGroup<EzyWsDataDecoder, EzyWsDataEncoder>
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
	protected EzyWsDataEncoder newEncoder(Object encoder) {
		return new EzySimpleWsDataEncoder((EzyWsObjectToByteEncoder)encoder);
	}
	
	@Override
	protected Object encodeData(Object data) throws Exception {
		return encoder.encode(data, String.class);
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
	
	@Override
	protected long getWriteBytesSize(Object bytes) {
		if(bytes instanceof String)
			return ((String)bytes).length();
		return super.getWriteBytesSize(bytes);
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

package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.callback.EzyCallback;
import com.tvd12.ezyfoxserver.codec.EzyMessage;

public class EzySimpleNioHandlerGroup
		extends EzyAbstractHandlerGroup<EzyNioDataDecoder, EzyNioDataEncoder>
		implements EzyNioHandlerGroup {
	
	private final EzyCallback<EzyMessage> decodeBytesCallback;

	public EzySimpleNioHandlerGroup(Builder builder) {
		super(builder);
		this.decodeBytesCallback = this::executeHandleReceivedMessage;
	}
	
	@Override
	protected EzyNioDataDecoder newDecoder(Object decoder) {
		return new EzySimpleNioDataDecoder((EzyNioByteToObjectDecoder)decoder);
	}
	
	@Override
	protected EzyNioDataEncoder newEncoder(Object encoder) {
		return new EzySimpleNioDataEncoder((EzyNioObjectToByteEncoder)encoder);
	}
	
	@Override
	protected Object encodeData(Object data) throws Exception {
		return encoder.encode(data);
	}
	
	@Override
	public void fireBytesReceived(byte[] bytes) throws Exception {
		handleReceivedBytes(bytes);
		executeAddReadBytes(bytes.length);
	}
	
	private synchronized void handleReceivedBytes(byte[] bytes) {
		try {
			decoder.decode(bytes, decodeBytesCallback);
		}
		catch(Throwable throwable) {
			fireExceptionCaught(throwable);
		}
	}
	
	private void executeHandleReceivedMessage(EzyMessage message) {
		codecThreadPool.execute(() -> handleReceivedMesssage(message));
	}
	
	private void handleReceivedMesssage(EzyMessage message) {
		Object data = decodeMessage(message);
		handlerThreadPool.execute(() -> handleReceivedData(data));
	}
	
	private Object decodeMessage(EzyMessage message) {
		try {
			return decoder.decode(message);
		}
		catch(Exception e) {
			getLogger().error("decode message error", e);
			return null;
		}
	}
	
	private void handleReceivedData(Object data) {
		try {
			handler.channelRead(data);
		} catch (Exception e) {
			getLogger().error("handle data error, data: " + data, e);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractHandlerGroup.Builder {

		@Override
		public EzyNioHandlerGroup build() {
			return new EzySimpleNioHandlerGroup(this);
		}
	}
	
}

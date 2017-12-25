package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.tvd12.ezyfoxserver.callback.EzyCallback;
import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.socket.EzyPacket;

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
		catch(Exception throwable) {
			fireExceptionCaught(throwable);
		}
	}
	
	private void executeHandleReceivedMessage(EzyMessage message) {
		codecThreadPool.execute(() -> handleReceivedMesssage(message));
	}
	
	private void handleReceivedMesssage(EzyMessage message) {
		Object data = decodeMessage(message);
		handleReceivedData(data);
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
	
	@Override
	protected int writePacketToSocket(EzyPacket packet) throws Exception {
		ByteBuffer buffer = (ByteBuffer)packet.getData();
		int bytesToWrite = buffer.remaining();
		int bytesWritten = channel.write(buffer);
		if (bytesWritten < bytesToWrite) {
			byte[] remainBytes = new byte[buffer.remaining()];
			ByteBuffer remainBuffer = ByteBuffer.wrap(remainBytes);
			SelectionKey selectionKey = getSession().getSelectionKey();
			if(selectionKey != null && selectionKey.isValid())
				selectionKey.interestOps(SelectionKey.OP_WRITE);
			packet.setFragment(remainBuffer);
			getLogger().debug("session {} write to socket has fragment, size = {}", getSession(), bytesToWrite - bytesWritten);
		}
		else {
			packet.release();
		}
		return bytesWritten;
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

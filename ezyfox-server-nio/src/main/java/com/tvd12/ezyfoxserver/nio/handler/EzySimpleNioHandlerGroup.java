package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.tvd12.ezyfox.callback.EzyCallback;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfoxserver.socket.EzyPacket;

public class EzySimpleNioHandlerGroup
		extends EzyAbstractHandlerGroup<EzyNioDataDecoder>
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
	protected void sendPacketNow0(EzyPacket packet) {
		ByteBuffer writeBuffer = ByteBuffer.allocate(packet.getSize());
		executeSendingPacket(packet, writeBuffer);
	}
	
	@Override
	protected int writePacketToSocket(EzyPacket packet, Object writeBuffer) throws Exception {
		byte[] bytes = getBytesToWrite(packet);
		int bytesToWrite = bytes.length;
		ByteBuffer buffer = getWriteBuffer((ByteBuffer)writeBuffer, bytesToWrite);
		buffer.clear();
		buffer.put(bytes);
		buffer.flip();
		int bytesWritten = channel.write(buffer);
		if (bytesWritten < bytesToWrite) {
			byte[] remainBytes = getPacketFragment(buffer);
			packet.setFragment(remainBytes);
			SelectionKey selectionKey = getSession().getSelectionKey();
			if(selectionKey != null && selectionKey.isValid()) {
				selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
			else {
				getLogger().warn("selection key invalid, wrriten bytes: {}, session: {}", bytesWritten, getSession());
			}
		}
		else {
			packet.release();
		}
		return bytesWritten;
	}
	
	private ByteBuffer getWriteBuffer(ByteBuffer fixed, int bytesToWrite) {
		return bytesToWrite > fixed.capacity() ? ByteBuffer.allocate(bytesToWrite) : fixed;
	}
	
	private byte[] getPacketFragment(ByteBuffer buffer) {
		byte[] remainBytes = new byte[buffer.remaining()];
		buffer.get(remainBytes);
		return remainBytes;
	}
	
	private byte[] getBytesToWrite(EzyPacket packet) {
		return (byte[])packet.getData();
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

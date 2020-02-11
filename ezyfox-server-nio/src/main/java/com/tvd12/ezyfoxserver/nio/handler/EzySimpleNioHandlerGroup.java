package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.tvd12.ezyfox.callback.EzyCallback;
import com.tvd12.ezyfox.codec.EzyByteToObjectDecoder;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageDataDecoder;
import com.tvd12.ezyfox.codec.EzyMessageHeader;
import com.tvd12.ezyfox.codec.EzySimpleMessageDataDecoder;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketStream;
import com.tvd12.ezyfoxserver.socket.EzySocketStream;

public class EzySimpleNioHandlerGroup
		extends EzyAbstractHandlerGroup<EzyMessageDataDecoder>
		implements EzyNioHandlerGroup {
	
	private final EzyCallback<EzyMessage> decodeBytesCallback;

	public EzySimpleNioHandlerGroup(Builder builder) {
		super(builder);
		this.decodeBytesCallback = m -> this.executeHandleReceivedMessage(m);
	}
	
	@Override
	protected EzyMessageDataDecoder newDecoder(Object decoder) {
		return new EzySimpleMessageDataDecoder((EzyByteToObjectDecoder)decoder);
	}
	
	@Override
	public void fireBytesReceived(byte[] bytes) throws Exception {
		handleReceivedBytes(bytes);
	}
	
	@Override
	public void fireMessageReceived(EzyMessage message) throws Exception {
		handleReceivedMesssage(message);
	}
	
	private void handleReceivedBytes(byte[] bytes) {
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
		try {
			EzyMessageHeader header = message.getHeader();
			if(header.isRawBytes()) {
				boolean sessionStreamingEnable = session.isStreamingEnable();
				if(!streamingEnable || !sessionStreamingEnable) {
					return;
				}
				byte[] rawBytes = message.getContent();
				EzySocketStream stream = new EzySimpleSocketStream(session, rawBytes);
				streamQueue.add(stream);
			}
			else {
				Object data = decodeMessage(message);
				int dataSize = message.getByteCount();
				handleReceivedData(data, dataSize);
			}
		}
		catch (Exception e) {
			fireExceptionCaught(e);
		}
		finally {
			executeAddReadBytes(message.getByteCount());
		}
	}
	
	private Object decodeMessage(EzyMessage message) throws Exception {
		Object answer = decoder.decode(message);
		return answer;
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
		int bytesWritten = channel.write(buffer, packet.isBinary());
		if (bytesWritten < bytesToWrite) {
			byte[] remainBytes = getPacketFragment(buffer);
			packet.setFragment(remainBytes);
			SelectionKey selectionKey = session.getSelectionKey();
			if(selectionKey != null && selectionKey.isValid()) {
				selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
			else {
				logger.warn("selection key invalid, wrriten bytes: {}, session: {}", bytesWritten, session);
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

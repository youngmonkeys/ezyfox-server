package com.tvd12.ezyfoxserver.nio.socket;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import static com.tvd12.ezyfoxserver.util.EzyProcessor.*;

import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;

public class EzySocketReader extends EzySocketHandler {

	protected Selector ownSelector;
	
	public EzySocketReader(Builder builder) {
		super(builder);
		this.ownSelector = builder.ownSelector;
	}
	
	@Override
	protected String getThreadName() {
		return "socket-reader";
	}
	
	@Override
	protected void tryDestroy() throws Exception {
		super.tryDestroy();
		processWithLogException(ownSelector::close);
	}
	
	@Override
	protected void tryLoop() {
		getLogger().info("socket-reader threadpool has started");
		ByteBuffer buffer = ByteBuffer.allocateDirect(getMaxBufferSize());
		while(active) {
			tryProcessReadyKeys(buffer);
		}
		getLogger().info("socket-reader threadpool shutting down");
	}
	
	private int getMaxBufferSize() {
		return 8192;
	}
	
	private void tryProcessReadyKeys(ByteBuffer buffer) {
		try {
			processReadyKeys(buffer);
			Thread.sleep(5L);
		}
		catch(Exception e) {
			getLogger().info("I/O error at socket-reader", e);
		}
	}
	
	private synchronized void processReadyKeys(ByteBuffer buffer) throws Exception {
		ownSelector.selectNow();
		
		Set<SelectionKey> readyKeys = this.ownSelector.selectedKeys();
		Iterator<SelectionKey> iterator = readyKeys.iterator();
		while(iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			if(key.isValid()) {
				processReadyKey(key, buffer);
			}
		}
	}
	
	private void processReadyKey(SelectionKey key, ByteBuffer buffer) throws Exception {
		buffer.clear();
		if(key.isWritable()) {
			processWritableKey(key, buffer);
		}
		if(key.isReadable()) {
			processReadableKey(key, buffer);
		}
	}
	
	private void processWritableKey(SelectionKey key, ByteBuffer buffer) throws Exception {
		
	}
	
	private void processReadableKey(SelectionKey key, ByteBuffer buffer) throws Exception {
		SocketChannel channel = (SocketChannel) key.channel();
		if(!channel.isConnected()) {
			return;
		}
		long readBytes = channel.read(buffer);
		if(readBytes == -1L) {
			closeConnection(channel);
		}
		else {
			processReadBytes(channel, buffer);
		}
	}
	
	private void processReadBytes(SocketChannel channel, ByteBuffer buffer) throws Exception {
		buffer.flip();
		byte[] binary = new byte[buffer.limit()];
		buffer.get(binary);
		EzyNioHandlerGroup group = handlerGroupManager.getHandlerGroup(channel);
		if(group != null) {
			group.fireBytesReceived(binary);
		}
		
	}
	
	private void closeConnection(SelectableChannel channel) throws Exception {
		handlerGroupManager.removeHandlerGroup((SocketChannel) channel);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySocketHandler.Builder<Builder> {
		
		private Selector ownSelector;
		
		public Builder ownSelector(Selector ownSelector) {
			this.ownSelector = ownSelector;
			return this;
		}
		
		@Override
		public EzySocketReader build() {
			return new EzySocketReader(this);
		}
	}
	
}

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
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;

import lombok.Setter;

public class EzyNioSocketReader 
		extends EzySocketAbstractEventHandler
		implements EzyHandlerGroupManagerAware {

	@Setter
	protected Selector ownSelector;
	@Setter
	protected EzyHandlerGroupManager handlerGroupManager;
	
	protected ByteBuffer buffer = ByteBuffer.allocateDirect(getMaxBufferSize());

	@Override
	public void destroy() {
		processWithLogException(ownSelector::close);
	}
	
	@Override
	public void handleEvent() {
		try {
			processReadyKeys(buffer);
			Thread.sleep(5L);
		}
		catch(Exception e) {
			getLogger().info("I/O error at socket-reader", e);
		}
	}
	
	private int getMaxBufferSize() {
		return 8192;
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
		key.interestOps(SelectionKey.OP_READ);
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
	
}

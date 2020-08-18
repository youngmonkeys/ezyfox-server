package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.tvd12.ezyfoxserver.constant.EzyCoreConstants;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
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
	@Setter
	protected EzyNioAcceptableConnectionsHandler acceptableConnectionsHandler;
	
	protected final ByteBuffer buffer = ByteBuffer.allocateDirect(getMaxBufferSize());

	@Override
	public void destroy() {
		processWithLogException(() -> ownSelector.close());
	}
	
	@Override
	public void handleEvent() {
		try {
			handleAcceptableConnections();
			processReadyKeys0();
			Thread.sleep(3L);
		}
		catch(Exception e) {
			logger.info("I/O error at socket-reader: {}({})", e.getClass().getName(), e.getMessage());
		}
	}
	
	private int getMaxBufferSize() {
		return EzyCoreConstants.MAX_READ_BUFFER_SIZE;
	}

	private void handleAcceptableConnections() {
		acceptableConnectionsHandler.handleAcceptableConnections();
	}
	
	private void processReadyKeys0() throws Exception {
		int readyKeyCount = ownSelector.selectNow();
		if(readyKeyCount > 0) {
			processReadyKeys();
		}
	}
	
	private void processReadyKeys() throws Exception {
		Set<SelectionKey> readyKeys = this.ownSelector.selectedKeys();
		Iterator<SelectionKey> iterator = readyKeys.iterator();
		while(iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			if(key.isValid()) {
				processReadyKey(key);
			}
		}
	}
	
	private void processReadyKey(SelectionKey key) throws Exception {
		if(key.isWritable()) {
			processWritableKey(key);
		}
		if(key.isReadable()) {
			processReadableKey(key);
		}
	}
	
	private void processWritableKey(SelectionKey key) throws Exception {
		key.interestOps(SelectionKey.OP_READ);
	}
	
	private void processReadableKey(SelectionKey key) throws Exception {
		int readBytes = -1;
		Exception exception = null;
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			buffer.clear();
			readBytes = channel.read(buffer);
		}
		catch (Exception e) {
			exception = e;
		}
		if(readBytes == -1) {
			closeConnection(channel);
		}
		else if(readBytes > 0) {
			processReadBytes(channel);
		}
		if(exception != null)
			logger.info("I/O error at socket-reader: {}({})", exception.getClass().getName(), exception.getMessage());
	}
	
	private void processReadBytes(SocketChannel channel) throws Exception {
		buffer.flip();
		byte[] binary = new byte[buffer.limit()];
		buffer.get(binary);
		EzyNioHandlerGroup group = handlerGroupManager.getHandlerGroup(channel);
		if(group != null) {
			group.fireBytesReceived(binary);
		}
		
	}
	
	private void closeConnection(SelectableChannel channel) throws Exception {
		closeConnection0(channel);
		processHandlerGroup(channel);
	}
	
	private void closeConnection0(SelectableChannel channel) throws Exception {
		channel.close();
	}
	
	private void processHandlerGroup(SelectableChannel channel) {
		EzyHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup((SocketChannel) channel);
		handlerGroup.enqueueDisconnection();
	}
	
}

package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;

import lombok.Setter;

public class EzyNioSocketReader extends EzySocketAbstractEventHandler {

	@Setter
	protected Selector ownSelector;
	@Setter
	protected EzySocketDataReceiver socketDataReceiver;
	@Setter
	protected EzyNioAcceptableConnectionsHandler acceptableConnectionsHandler;

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
		socketDataReceiver.tcpReceive((SocketChannel) key.channel());
	}
	
}

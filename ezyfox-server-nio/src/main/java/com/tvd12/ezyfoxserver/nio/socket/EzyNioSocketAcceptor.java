package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;

import lombok.Setter;

public class EzyNioSocketAcceptor 
		extends EzySocketAbstractEventHandler
		implements EzyHandlerGroupManagerAware, EzyNioAcceptableConnectionsHandler {

	@Setter
	private boolean tcpNoDelay;
	@Setter
	private Selector ownSelector;
	@Setter
	private Selector readSelector;
	@Setter
	private List<SocketChannel> acceptableConnections;
	@Setter
	protected EzyHandlerGroupManager handlerGroupManager;
	
	@Override
	public void destroy() {
		processWithLogException(ownSelector::close);
	}

	@Override
	public void handleEvent() {
		try {
			processReadyKeys();
		}
		catch(Exception e) {
			getLogger().info("I/O error at socket-acceptor", e);
		}
	}
	
	public void handleAcceptableConnections() {
		if(acceptableConnections.isEmpty()) {
			return;
		}
		synchronized (acceptableConnections) {
			handleAcceptableConnections0();
		}
	}
	
	private void processReadyKeys() throws Exception {
		ownSelector.select();
		
		Set<SelectionKey> readyKeys = ownSelector.selectedKeys();
		Iterator<SelectionKey> iterator = readyKeys.iterator();
		while(iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			processReadyKey(key);
		}
		readSelector.wakeup();
	}
	
	private void processReadyKey(SelectionKey key) throws Exception {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		if(key.isAcceptable()) {
			SocketChannel clientChannel = serverChannel.accept();
			addConnection(clientChannel);
		}
	}
	
	private void addConnection(SocketChannel clientChannel) {
		synchronized (acceptableConnections) {
			acceptableConnections.add(clientChannel);
		}
	}
	
	private void handleAcceptableConnections0() {
		Iterator<SocketChannel> iterator = acceptableConnections.iterator();
		while(iterator.hasNext()) {
			SocketChannel clientChannel = iterator.next();
			iterator.remove();
			acceptConnection(clientChannel);
		}
	}
	
	private void acceptConnection(SocketChannel clientChannel) {
		try {
			acceptConnection0(clientChannel);
		}
		catch(Exception e) {
			getLogger().error("can't acception connection: " + clientChannel, e);
		}
	}
	
	private void acceptConnection0(SocketChannel clientChannel) throws Exception {
		clientChannel.configureBlocking(false);
		clientChannel.socket().setTcpNoDelay(tcpNoDelay);
		
		EzyChannel channel = new EzyNioSocketChannel(clientChannel);
		
		EzyNioHandlerGroup dataHandler = handlerGroupManager
				.newHandlerGroup(channel, EzyConnectionType.SOCKET);
		EzyNioSession session = dataHandler.getSession();

		SelectionKey selectionKey = clientChannel.register(readSelector, SelectionKey.OP_READ);
		session.setProperty(EzyNioSession.SELECTION_KEY, selectionKey);
	}
	
}

package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyChannel;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;

public class EzySocketAcceptor extends EzySocketHandler {

	private boolean tcpNoDelay;
	private Selector ownSelector;
	private Selector readSelector;
	
	public EzySocketAcceptor(Builder builder) {
		super(builder);
		this.tcpNoDelay = builder.tcpNoDelay;
		this.ownSelector = builder.ownSelector;
		this.readSelector = builder.readSelector;
	}

	@Override
	protected String getThreadName() {
		return "socket-acceptor";
	}
	
	@Override
	protected void tryDestroy() throws Exception {
		super.tryDestroy();
		processWithLogException(ownSelector::close);
	}
	
	@Override
	protected void tryLoop() {
		getLogger().info("socket-acceptor threadpool has started");
		while(active) {
			tryProcessReadyKeys();
		}
		getLogger().info("socket-acceptor threadpool shutting down");
	}
	
	private void tryProcessReadyKeys() {
		try {
			processReadyKeys();
		}
		catch(Exception e) {
			getLogger().info("I/O error at socket-acceptor", e);
		}
	}
	
	private synchronized void processReadyKeys() throws Exception {
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
			acceptConnection(clientChannel);
		}
	}
	
	private void acceptConnection(SocketChannel clientChannel) throws Exception {
		clientChannel.configureBlocking(false);
		clientChannel.socket().setTcpNoDelay(tcpNoDelay);
		
		EzyChannel channel = new EzySocketChannel(clientChannel);
		
		EzyNioHandlerGroup dataHandler = handlerGroupManager
				.newHandlerGroup(channel, EzyConnectionType.SOCKET);
		EzyNioSession session = dataHandler.fireChannelActive();

		SelectionKey selectionKey = clientChannel.register(readSelector, SelectionKey.OP_READ);
		session.setProperty(EzyNioSession.SELECTION_KEY, selectionKey);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySocketHandler.Builder<Builder> {
		
		private boolean tcpNoDelay;
		private Selector ownSelector;
		private Selector readSelector;
		
		public Builder tcpNoDelay(boolean tcpNoDelay) {
			this.tcpNoDelay = tcpNoDelay;
			return this;
		}
		
		public Builder ownSelector(Selector ownSelector) {
			this.ownSelector = ownSelector;
			return this;
		}
		
		public Builder readSelector(Selector readSelector) {
			this.readSelector = readSelector;
			return this;
		}
		
		@Override
		public EzySocketAcceptor build() {
			return new EzySocketAcceptor(this);
		}
	}
	
}

package com.tvd12.ezyfoxserver.nio;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptanceLoopHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketReader;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketReadingLoopHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketWriter;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketWritingLoopHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public class EzySocketServerBootstrap implements EzyStartable, EzyDestroyable {

	private Selector readSelector;
	private Selector acceptSelector;
	private ServerSocket serverSocket;
	private ServerSocketChannel serverSocketChannel;
	
	private EzyServerContext serverContext;
	private EzyHandlerGroupManager handlerGroupManager;
	private EzySessionTicketsQueue sessionTicketsQueue;
	
	private EzySocketEventLoopHandler writingLoopHandler;
	private EzySocketEventLoopHandler readingLoopHandler;
	private EzySocketEventLoopHandler acceptanceLoopHandler;
	
	public EzySocketServerBootstrap(Builder builder) {
		this.serverContext = builder.serverContext;
		this.handlerGroupManager = builder.handlerGroupManager;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
	}
	
	@Override
	public void start() throws Exception {
		openSelectors();
		newAndConfigServerSocketChannel();
		getBindAndConfigServerSocket();
		startSocketHandlers();
	}
	
	@Override
	public void destroy() {
		processWithLogException(writingLoopHandler::destroy);
		processWithLogException(readingLoopHandler::destroy);
		processWithLogException(acceptanceLoopHandler::destroy);
		processWithLogException(serverSocket::close);
		processWithLogException(serverSocketChannel::close);
	}
	
	private void openSelectors() throws Exception {
		this.readSelector = openSelector();
		this.acceptSelector = openSelector();
	}
	
	private void newAndConfigServerSocketChannel() throws Exception {
		this.serverSocketChannel = newServerSocketChannel();
		this.serverSocketChannel.configureBlocking(false);
	}
	
	private void getBindAndConfigServerSocket() throws Exception {
		this.serverSocket = serverSocketChannel.socket();
		this.serverSocket.setReuseAddress(true);
		this.serverSocket.bind(new InetSocketAddress(getSocketAddress(), getSocketPort()));
		this.serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
	}
	
	private void startSocketHandlers() throws Exception {
		writingLoopHandler = newWritingLoopHandler();
		readingLoopHandler = newReadingLoopHandler();
		acceptanceLoopHandler = newAcceptanceLoopHandler();
		acceptanceLoopHandler.start();
		readingLoopHandler.start();
		writingLoopHandler.start();
	}
	
	private EzySocketEventLoopHandler newWritingLoopHandler() {
		EzySocketEventLoopHandler loopHandler = new EzyNioSocketWritingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketWriterPoolSize());
		EzyNioSocketWriter eventHandler = new EzyNioSocketWriter();
		eventHandler.setHandlerGroupManager(handlerGroupManager);
		eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopHandler newReadingLoopHandler() {
		EzySocketEventLoopHandler loopHandler = new EzyNioSocketReadingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketReaderPoolSize());
		EzyNioSocketReader eventHandler = new EzyNioSocketReader();
		eventHandler.setOwnSelector(readSelector);
		eventHandler.setHandlerGroupManager(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopHandler newAcceptanceLoopHandler() {
		EzySocketEventLoopHandler loopHandler = new EzyNioSocketAcceptanceLoopHandler();
		loopHandler.setThreadPoolSize(getSocketAcceptorPoolSize());
		EzyNioSocketAcceptor eventHandler = new EzyNioSocketAcceptor();
		eventHandler.setTcpNoDelay(true);
		eventHandler.setReadSelector(readSelector);
		eventHandler.setOwnSelector(acceptSelector);
		eventHandler.setHandlerGroupManager(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
 	
	private Selector openSelector() throws Exception {
		return Selector.open();
	}
	
	private ServerSocketChannel newServerSocketChannel() throws Exception {
		return ServerSocketChannel.open();
	}
	
	private int getSocketReaderPoolSize() {
		return 3;
	}
	
	private int getSocketWriterPoolSize() {
		return 8;
	}
	
	private int getSocketAcceptorPoolSize() {
		return 3;
	}
	
	private int getSocketPort() {
		return getSocketSettings().getPort();
	}
	
	private String getSocketAddress() {
		return getSocketSettings().getAddress();
	}
	
	private EzySocketSetting getSocketSettings() {
		return serverContext.getServer().getSettings().getSocket();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzySocketServerBootstrap> {

		private EzyServerContext serverContext;
		private EzyHandlerGroupManager handlerGroupManager;
		private EzySessionTicketsQueue sessionTicketsQueue;
		
		public Builder serverContext(EzyServerContext context) {
			this.serverContext = context;
			return this;
		}
		
		public Builder handlerGroupManager(EzyHandlerGroupManager manager) {
			this.handlerGroupManager = manager;
			return this;
		}
		
		public Builder sessionTicketsQueue(EzySessionTicketsQueue queue) {
			this.sessionTicketsQueue = queue;
			return this;
		}
		
		@Override
		public EzySocketServerBootstrap build() {
			return new EzySocketServerBootstrap(this);
		}
	}
}

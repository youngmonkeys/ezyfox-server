package com.tvd12.ezyfoxserver.nio;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;

import com.tvd12.ezyfoxserver.nio.constant.EzyNioThreadPoolSizes;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioAcceptableConnectionsHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptanceLoopHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketReader;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketReadingLoopHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketWriter;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;

public class EzySocketServerBootstrap extends EzyAbstractSocketServerBootstrap {

	private Selector readSelector;
	private Selector acceptSelector;
	private ServerSocket serverSocket;
	private ServerSocketChannel serverSocketChannel;
	
	private EzySocketEventLoopHandler readingLoopHandler;
	private EzySocketEventLoopHandler socketAcceptanceLoopHandler;
	
	public EzySocketServerBootstrap(Builder builder) {
		super(builder);
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
		processWithLogException(() -> writingLoopHandler.destroy());
		processWithLogException(() -> readingLoopHandler.destroy());
		processWithLogException(() -> socketAcceptanceLoopHandler.destroy());
		processWithLogException(() -> serverSocket.close());
		processWithLogException(() -> serverSocketChannel.close());
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
		EzyNioSocketAcceptor socketAcceptor = new EzyNioSocketAcceptor();
		writingLoopHandler = newWritingLoopHandler();
		readingLoopHandler = newReadingLoopHandler(socketAcceptor);
		socketAcceptanceLoopHandler = newSocketAcceptanceLoopHandler(socketAcceptor);
		socketAcceptanceLoopHandler.start();
		readingLoopHandler.start();
		writingLoopHandler.start();
	}
	
	private EzySocketEventLoopHandler newWritingLoopHandler() {
		EzySocketWritingLoopHandler loopHandler = new EzySocketWritingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketWriterPoolSize());
		loopHandler.setEventHandlerSupplier(() -> {
			EzySocketWriter eventHandler = new EzyNioSocketWriter();
			eventHandler.setWriterGroupFetcher(handlerGroupManager);
			eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
			return eventHandler;
		});
		return loopHandler;
	}
	
	private EzySocketEventLoopHandler newReadingLoopHandler(
			EzyNioAcceptableConnectionsHandler acceptableConnectionsHandler) {
		EzySocketEventLoopOneHandler loopHandler = new EzyNioSocketReadingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketReaderPoolSize());
		EzyNioSocketReader eventHandler = new EzyNioSocketReader();
		eventHandler.setOwnSelector(readSelector);
		eventHandler.setHandlerGroupManager(handlerGroupManager);
		eventHandler.setAcceptableConnectionsHandler(acceptableConnectionsHandler);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopHandler newSocketAcceptanceLoopHandler(
			EzyNioSocketAcceptor socketAcceptor) {
		EzySocketEventLoopOneHandler loopHandler = new EzyNioSocketAcceptanceLoopHandler();
		loopHandler.setThreadPoolSize(getSocketAcceptorPoolSize());
		socketAcceptor.setTcpNoDelay(false);
		socketAcceptor.setReadSelector(readSelector);
		socketAcceptor.setOwnSelector(acceptSelector);
		socketAcceptor.setAcceptableConnections(new ArrayList<>());
		socketAcceptor.setHandlerGroupManager(handlerGroupManager);
		loopHandler.setEventHandler(socketAcceptor);
		return loopHandler;
	}
	
	private Selector openSelector() throws Exception {
		return Selector.open();
	}
	
	private ServerSocketChannel newServerSocketChannel() throws Exception {
		return ServerSocketChannel.open();
	}
	
	private int getSocketReaderPoolSize() {
		return EzyNioThreadPoolSizes.SOCKET_READER;
	}
	
	private int getSocketWriterPoolSize() {
		return EzyNioThreadPoolSizes.SOCKET_WRITER;
	}
	
	private int getSocketAcceptorPoolSize() {
		return EzyNioThreadPoolSizes.SOCKET_ACCEPTOR;
	}
	
	private int getSocketPort() {
		return getSocketSetting().getPort();
	}
	
	private String getSocketAddress() {
		return getSocketSetting().getAddress();
	}
	
	private EzySocketSetting getSocketSetting() {
		return getServerSettings().getSocket();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder 
			extends EzyAbstractSocketServerBootstrap.Builder<Builder, EzySocketServerBootstrap> {

		@Override
		public EzySocketServerBootstrap build() {
			return new EzySocketServerBootstrap(this);
		}
		
	}
}

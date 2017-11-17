package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketReader;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.*;

public class EzySocketServerBootstrap implements EzyStartable, EzyDestroyable {

	private Selector readSelector;
	private Selector acceptSelector;
	private ServerSocket serverSocket;
	private ServerSocketChannel serverSocketChannel;
	
	private EzySocketWriter socketWriter;
	private EzySocketReader socketReader;
	private EzySocketAcceptor socketAcceptor;
	
	private EzyServerContext serverContext;
	private EzyHandlerGroupManager handlerGroupManager;
	private EzySessionTicketsQueue sessionTicketsQueue;
	
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
		processWithLogException(socketAcceptor::destroy);
		processWithLogException(socketWriter::destroy);
		processWithLogException(socketReader::destroy);
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
		socketWriter = newSocketWriter();
		socketReader = newSocketReader();
		socketAcceptor = newSocketAcceptor();
		socketAcceptor.start();
		socketReader.start();
		socketWriter.start();
	}
	
	private EzySocketAcceptor newSocketAcceptor() {
		return newSocketHandler(EzySocketAcceptor.builder()
				.tcpNoDelay(true)
				.readSelector(readSelector)
				.ownSelector(acceptSelector)
				.threadPoolSize(getAcceptorPoolSize()));
	}
	
	private EzySocketReader newSocketReader() {
		return newSocketHandler(EzySocketReader.builder()
				.ownSelector(readSelector)
				.threadPoolSize(getReaderPoolSize()));
	}
	
	private EzySocketWriter newSocketWriter() {
		return newSocketHandler(EzySocketWriter.builder()
				.threadPoolSize(getWriterPoolSize())
				.sessionTicketsQueue(sessionTicketsQueue));
	}
	
	@SuppressWarnings("unchecked")
	private <T> T newSocketHandler(EzySocketHandler.Builder<?> builder) {
		return (T)builder
				.handlerGroupManager(handlerGroupManager)
				.build();
	}
	
 	
	private Selector openSelector() throws Exception {
		return Selector.open();
	}
	
	private ServerSocketChannel newServerSocketChannel() throws Exception {
		return ServerSocketChannel.open();
	}
	
	private int getReaderPoolSize() {
		return 3;
	}
	
	private int getWriterPoolSize() {
		return 3;
	}
	
	private int getAcceptorPoolSize() {
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

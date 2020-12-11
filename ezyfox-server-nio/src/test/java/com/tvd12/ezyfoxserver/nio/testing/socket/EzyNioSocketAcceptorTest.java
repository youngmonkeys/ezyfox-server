package com.tvd12.ezyfoxserver.nio.testing.socket;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.codec.EzyByteToObjectDecoder;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageHeader;
import com.tvd12.ezyfox.codec.EzySimpleMessage;
import com.tvd12.ezyfox.codec.EzySimpleMessageHeader;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;

public class EzyNioSocketAcceptorTest extends BaseTest {

	@Test
	public void test() throws Exception {
		EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager();
		
		Selector ownSelector = spy(ExSelector.class);
		SelectionKey selectionKey1 = spy(ExSelectionKey.class);
		SelectionKey selectionKey2 = spy(ExSelectionKey.class);
		when(ownSelector.selectedKeys()).thenReturn(Sets.newHashSet(
				selectionKey1, selectionKey2)); 
		when(selectionKey1.isValid()).thenReturn(true);
		when(selectionKey1.readyOps()).thenReturn(SelectionKey.OP_ACCEPT);
		ExServerSocketChannel serverChannel1 = spy(ExServerSocketChannel.class);
		when(selectionKey1.channel()).thenReturn(serverChannel1);
		SocketChannel channel1 = spy(ExSocketChannel.class);
		when(serverChannel1.accept()).thenReturn(channel1);
		Socket socket1 = new Socket();
		when(channel1.socket()).thenReturn(socket1);
		
		Selector readSelector = spy(ExSelector.class);
		
		channel1.configureBlocking(false);
		when(channel1.register(readSelector, SelectionKey.OP_READ)).thenReturn(selectionKey1);
		
		EzyNioSocketAcceptor acceptor = new EzyNioSocketAcceptor();
		acceptor.setAcceptableConnections(new ArrayList<>());
		acceptor.setHandlerGroupManager(handlerGroupManager);
		acceptor.setOwnSelector(ownSelector);
		acceptor.setReadSelector(readSelector);
		acceptor.handleEvent();
		List<SocketChannel> acceptableConnections = FieldUtil.getFieldValue(acceptor, "acceptableConnections");
		assert acceptableConnections.size() == 1;
		acceptor.handleAcceptableConnections();
		assert acceptableConnections.size() == 0;
	}
	
	@Test
	public void acceptConnectionExceptionCase() throws Exception {
		EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager();
		
		Selector ownSelector = spy(ExSelector.class);
		SelectionKey selectionKey1 = spy(ExSelectionKey.class);
		SelectionKey selectionKey2 = spy(ExSelectionKey.class);
		when(ownSelector.selectedKeys()).thenReturn(Sets.newHashSet(
				selectionKey1, selectionKey2)); 
		when(selectionKey1.isValid()).thenReturn(true);
		when(selectionKey1.readyOps()).thenReturn(SelectionKey.OP_ACCEPT);
		ExServerSocketChannel serverChannel1 = spy(ExServerSocketChannel.class);
		when(selectionKey1.channel()).thenReturn(serverChannel1);
		SocketChannel channel1 = spy(ExSocketChannel.class);
		when(serverChannel1.accept()).thenReturn(channel1);
		when(channel1.socket()).thenThrow(new IllegalStateException("server maintain"));
		
		Selector readSelector = spy(ExSelector.class);
		
		EzyNioSocketAcceptor acceptor = new EzyNioSocketAcceptor();
		acceptor.setAcceptableConnections(new ArrayList<>());
		acceptor.setHandlerGroupManager(handlerGroupManager);
		acceptor.setOwnSelector(ownSelector);
		acceptor.setReadSelector(readSelector);
		acceptor.handleEvent();
		List<SocketChannel> acceptableConnections = FieldUtil.getFieldValue(acceptor, "acceptableConnections");
		assert acceptableConnections.size() == 1;
		acceptor.handleAcceptableConnections();
		assert acceptableConnections.size() == 0;
	}
	
	
	private EzyHandlerGroupManager newHandlerGroupManager() {
		EzyNioSessionManager sessionManager = (EzyNioSessionManager)EzyNioSessionManagerImpl.builder()
				.tokenGenerator(new EzySimpleSessionTokenGenerator())
				.build();
		ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
		EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
		when(codecFactory.newDecoder(any())).thenReturn(decoder);
		ExecutorService statsThreadPool = EzyExecutors.newSingleThreadExecutor("stats");
		ExecutorService codecThreadPool = EzyExecutors.newSingleThreadExecutor("codec");
		EzySocketRequestQueues requestQueues = new EzySimpleSocketRequestQueues();
		EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
		EzySocketDisconnectionQueue disconnectionQueue = new EzyBlockingSocketDisconnectionQueue();
		
		EzySimpleSettings settings = new EzySimpleSettings();
		EzySimpleStreamingSetting streaming = settings.getStreaming();
		streaming.setEnable(true);
		EzySimpleServer server = new EzySimpleServer();
		server.setSettings(settings);
		server.setSessionManager(sessionManager);
		EzySimpleServerContext serverContext = new EzySimpleServerContext();
		serverContext.setServer(server);
		serverContext.init();
		
		EzySessionTicketsQueue socketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
		EzySessionTicketsQueue webSocketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
		EzyStatistics statistics = new EzySimpleStatistics();
		EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
				.statistics(statistics)
				.socketSessionTicketsQueue(socketSessionTicketsQueue)
				.webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
				.build();
		
		EzyHandlerGroupManager handlerGroupManager = EzyHandlerGroupManagerImpl.builder()
				.statsThreadPool(statsThreadPool)
				.codecThreadPool(codecThreadPool)
				.streamQueue(streamQueue)
				.requestQueues(requestQueues)
				.disconnectionQueue(disconnectionQueue)
				.codecFactory(codecFactory)
				.serverContext(serverContext)
				.handlerGroupBuilderFactory(handlerGroupBuilderFactory)
				.build();
		return handlerGroupManager;
	}
	
	public static abstract class ExServerSocketChannel extends ServerSocketChannel {
		public ExServerSocketChannel() {
			super(SelectorProvider.provider());
		}
	}
	
	
	public static abstract class ExSocketChannel extends SocketChannel {

		public ExSocketChannel() {
			super(SelectorProvider.provider());
		}
		
	}
	
	public static abstract class ExSelector extends AbstractSelector {
		public ExSelector() {
			super(SelectorProvider.provider());
		}
	}
	
	public static abstract class ExSelectionKey extends SelectionKey {
		public ExSelectionKey() {}
	}
	
	public static class ExEzyByteToObjectDecoder implements EzyByteToObjectDecoder {

		@Override
		public void reset() {}

		@Override
		public Object decode(EzyMessage message) throws Exception {
			return EzyEntityFactory.newArrayBuilder()
					.append(EzyCommand.PING.getId())
					.build();
		}

		@Override
		public void decode(ByteBuffer bytes, Queue<EzyMessage> queue) throws Exception {
			EzyMessageHeader header = new EzySimpleMessageHeader(false, false, false, false, false, false);
			byte[] content = new byte[bytes.remaining()];
			bytes.get(content);
			EzyMessage message = new EzySimpleMessage(header, content, content.length);
			queue.add(message);
		}
		
	}
}

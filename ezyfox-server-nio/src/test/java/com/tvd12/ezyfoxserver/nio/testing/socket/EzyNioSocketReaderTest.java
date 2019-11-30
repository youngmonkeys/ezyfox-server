package com.tvd12.ezyfoxserver.nio.testing.socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketReader;
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

public class EzyNioSocketReaderTest extends BaseTest {

	@Test
	public void test() throws Exception {
		EzyNioSessionManager sessionManager = (EzyNioSessionManager)EzyNioSessionManagerImpl.builder()
				.tokenGenerator(new EzySimpleSessionTokenGenerator())
				.build();
		EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
		ExecutorService statsThreadPool = EzyExecutors.newSingleThreadExecutor("stats");
		ExecutorService codecThreadPool = EzyExecutors.newSingleThreadExecutor("codec");
		EzySocketRequestQueues requestQueues = new EzySimpleSocketRequestQueues();
		EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
		EzySocketDisconnectionQueue disconnectionQueue = EzyBlockingSocketDisconnectionQueue.getInstance();
		
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
		
		
		Selector ownSelector = spy(ExSelector.class);
		when(ownSelector.selectNow()).thenReturn(1);
		SelectionKey selectionKey1 = spy(ExSelectionKey.class);
		SelectionKey selectionKey2 = spy(ExSelectionKey.class);
		SelectionKey selectionKey3 = spy(ExSelectionKey.class);
		when(ownSelector.selectedKeys()).thenReturn(Sets.newHashSet(
				selectionKey1, selectionKey2, selectionKey3));
		when(selectionKey1.isValid()).thenReturn(true);
		when(selectionKey1.readyOps()).thenReturn(SelectionKey.OP_READ);
		when(selectionKey2.isValid()).thenReturn(true);
		when(selectionKey2.readyOps()).thenReturn(SelectionKey.OP_WRITE);
		when(selectionKey3.isValid()).thenReturn(false);
		
		SocketChannel socketChannel = mock(SocketChannel.class);
		when(selectionKey1.channel()).thenReturn(socketChannel);
		
		EzyNioSocketAcceptor socketAcceptor = new EzyNioSocketAcceptor();
		socketAcceptor.setReadSelector(ownSelector);
		socketAcceptor.setHandlerGroupManager(handlerGroupManager);
		socketAcceptor.setAcceptableConnections(new ArrayList<>());
		
		EzyNioSocketReader socketReader = new EzyNioSocketReader();
		socketReader.setOwnSelector(ownSelector);
		socketReader.setAcceptableConnectionsHandler(socketAcceptor);
		socketReader.setHandlerGroupManager(handlerGroupManager);
		socketReader.handleEvent();
	}
	
	public static abstract class ExSelector extends Selector {
		public ExSelector() {}
	}
	
	public static abstract class ExSelectionKey extends SelectionKey {
		public ExSelectionKey() {}
	}
}

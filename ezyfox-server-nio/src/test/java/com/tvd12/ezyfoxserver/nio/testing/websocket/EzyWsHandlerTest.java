package com.tvd12.ezyfoxserver.nio.testing.websocket;

import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.websocket.api.Session;
import org.testng.annotations.Test;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
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

public class EzyWsHandlerTest extends BaseTest {

	@Test
	public void test() throws Exception {
		EzySimpleSessionManagementSetting sessionManagementSetting = new EzySimpleSessionManagementSetting();
		EzyNioSessionManager sessionManager = (EzyNioSessionManager)EzyNioSessionManagerImpl.builder()
				.tokenGenerator(new EzySimpleSessionTokenGenerator())
				.build();
		EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
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
				
		EzyWsHandler handler = EzyWsHandler.builder()
				.sessionManagementSetting(sessionManagementSetting)
				.sessionManager(sessionManager)
				.handlerGroupManager(handlerGroupManager)
				.build();
		
		Session session = mock(Session.class);
		handler.onConnect(session);
		handler.onMessage(session, "hello");
		handler.onMessage(session, "hello".getBytes(), 0, 5);
		handler.onError(session, new TimeoutException("timeout"));
		handler.onError(session, new IllegalStateException("maintain"));
		handler.onClose(session, 39999, "test");
		handler.onClose(session, 3000, "test");
		Session session2 = mock(Session.class);
		handler.onMessage(session2, "hello");
		handler.onMessage(session2, "hello".getBytes(), 0, 5);
		handler.onError(session2, new TimeoutException("timeout"));
		handler.onError(session2, new IllegalStateException("maintain"));
		handler.onClose(session2, 39999, "test");
		handler.onClose(session2, 3000, "test");
	}
	
}

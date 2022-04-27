package com.tvd12.ezyfoxserver.nio.testing.websocket;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.eclipse.jetty.websocket.api.Session;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

public class EzyWsHandlerTest extends BaseTest {

    @Test
    public void test() throws Exception {
        EzySimpleSessionManagementSetting sessionManagementSetting = new EzySimpleSessionManagementSetting();
        EzyNioSessionManager sessionManager = (EzyNioSessionManager) EzyNioSessionManagerImpl.builder()
            .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
            .tokenGenerator(new EzySimpleSessionTokenGenerator())
            .build();
        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        ExecutorService statsThreadPool = EzyExecutors.newSingleThreadExecutor("stats");
        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySocketDisconnectionQueue disconnectionQueue = new EzyBlockingSocketDisconnectionQueue();
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

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
            .statsThreadPool(statsThreadPool)
            .streamQueue(streamQueue)
            .disconnectionQueue(disconnectionQueue)
            .codecFactory(codecFactory)
            .serverContext(serverContext)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        EzyHandlerGroupManager handlerGroupManager = EzyHandlerGroupManagerImpl.builder()
            .handlerGroupBuilderFactory(handlerGroupBuilderFactory)
            .build();

        EzySocketDataReceiver socketDataReceiver = EzySocketDataReceiver.builder()
            .threadPoolSize(1)
            .handlerGroupManager(handlerGroupManager)
            .build();

        EzyWsHandler handler = EzyWsHandler.builder()
            .sessionManagementSetting(sessionManagementSetting)
            .sessionManager(sessionManager)
            .handlerGroupManager(handlerGroupManager)
            .socketDataReceiver(socketDataReceiver)
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

    @Test
    public void setChannelClosedWithChannelNull() {
        // given
        EzyNioSessionManager sessionManager = mock(EzyNioSessionManager.class);

        EzyWsHandler sut = EzyWsHandler.builder()
            .sessionManager(sessionManager)
            .build();
        Session connection = mock(Session.class);
        EzyNioSession session = mock(EzyNioSession.class);

        when(sessionManager.getSession(connection)).thenReturn(session);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("setChannelClosed")
            .param(Session.class, connection)
            .call();

        // then
        verify(sessionManager, times(1)).getSession(connection);
        verify(session, times(1)).getChannel();
    }
}

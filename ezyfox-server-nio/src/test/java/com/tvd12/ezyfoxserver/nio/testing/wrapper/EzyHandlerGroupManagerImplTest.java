package com.tvd12.ezyfoxserver.nio.testing.wrapper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;

import org.eclipse.jetty.websocket.api.Session;
import org.testng.annotations.Test;

import com.tvd12.ezyfox.codec.EzyStringToObjectDecoder;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
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
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.test.base.BaseTest;

public class EzyHandlerGroupManagerImplTest extends BaseTest {

    @Test
    public void test() {
        EzyNioSessionManager sessionManager = (EzyNioSessionManager)EzyNioSessionManagerImpl.builder()
                .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
                .tokenGenerator(new EzySimpleSessionTokenGenerator())
                .build();
        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        when(codecFactory.newDecoder(any())).thenReturn(decoder);
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
                .socketSessionTicketsQueue(webSocketSessionTicketsQueue)
                .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
                .build();

        EzyHandlerGroupManager handlerGroupManager = EzyHandlerGroupManagerImpl.builder()
                .handlerGroupBuilderFactory(handlerGroupBuilderFactory)
                .build();
        handlerGroupManager.removeHandlerGroup(null);
        EzySession session1 = mock(EzyAbstractSession.class);
        handlerGroupManager.removeHandlerGroup(session1);
        EzySession session2 = mock(EzyAbstractSession.class);
        EzyChannel channel2 = mock(EzyChannel.class);
        when(session2.getChannel()).thenReturn(channel2);
        handlerGroupManager.removeHandlerGroup(session2);
        EzyChannel channel3 = mock(EzyChannel.class);
        Session connection3 = mock(Session.class);
        when(channel3.getConnection()).thenReturn(connection3);
        EzyHandlerGroup handlerGroup3 = handlerGroupManager.newHandlerGroup(channel3, EzyConnectionType.WEBSOCKET);
        EzySession session3 = mock(EzyAbstractSession.class);
        when(session3.getChannel()).thenReturn(channel3);
        assert handlerGroupManager.getDataHandlerGroup(null) == null;
        assert handlerGroupManager.getDataHandlerGroup(session1) == null;
        assert handlerGroupManager.getDataHandlerGroup(session2) == null;
        assert handlerGroupManager.getWriterGroup(session3) == handlerGroup3;
        InetSocketAddress udpAddress = new InetSocketAddress("127.0.0.1", 12345);
        handlerGroupManager.mapSocketChannel(udpAddress, session3);
        assert handlerGroupManager.getSocketChannel(udpAddress) != null;
        handlerGroupManager.unmapHandlerGroup(udpAddress);
        handlerGroupManager.removeHandlerGroup(session3);
        handlerGroupManager.destroy();
    }

    @Test
    public void unmapHandlerGroupNullAddress() {
        // given
        EzyHandlerGroupManager sut = newHandlerGroupManager();

        // when
        // then
        sut.unmapHandlerGroup(null);
    }

    @Test
    public void mapSocketChannelSessionNull() {
        // given
        EzyHandlerGroupManager sut = newHandlerGroupManager();

        // when
        // then
        sut.mapSocketChannel(null, null);
    }

    @Test
    public void mapSocketChannelChannelNull() {
        // given
        EzyHandlerGroupManager sut = newHandlerGroupManager();

        EzySession session = mock(EzySession.class);

        // when
        sut.mapSocketChannel(null, session);

        // then
        verify(session, times(1)).getChannel();
    }

    @Test
    public void mapSocketChannelConnectionNull() {
        // given
        EzyHandlerGroupManager sut = newHandlerGroupManager();

        EzySession session = mock(EzySession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);

        // when
        sut.mapSocketChannel(null, session);

        // then
        verify(session, times(1)).getChannel();
    }

    @Test
    public void mapSocketChannelNonContainsConnection() {
        // given
        EzyHandlerGroupManager sut = newHandlerGroupManager();

        EzySession session = mock(EzySession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);

        Object connection = new Object();
        when(channel.getConnection()).thenReturn(connection);

        // when
        sut.mapSocketChannel(null, session);

        // then
        verify(session, times(1)).getChannel();
        verify(channel, times(1)).getConnection();
    }

    @Test
    public void removeHandlerGroupUdpClientAddressIsNull() {
        // given
        EzyHandlerGroupManager sut = newHandlerGroupManager();

        EzySession session = mock(EzySession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);

        Object connection = new Object();
        when(channel.getConnection()).thenReturn(connection);

        SocketAddress udpClientAddress = mock(SocketAddress.class);
        when(session.getUdpClientAddress()).thenReturn(udpClientAddress);

        // when
        sut.removeHandlerGroup(session);

        // then
        verify(session, times(1)).getChannel();
        verify(session, times(1)).getUdpClientAddress();
        verify(channel, times(1)).getConnection();
    }

    public EzyHandlerGroupManager newHandlerGroupManager() {
        EzyNioSessionManager sessionManager = (EzyNioSessionManager)EzyNioSessionManagerImpl.builder()
                .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
                .tokenGenerator(new EzySimpleSessionTokenGenerator())
                .build();
        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        when(codecFactory.newDecoder(any())).thenReturn(decoder);
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
                .socketSessionTicketsQueue(webSocketSessionTicketsQueue)
                .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
                .build();

        EzyHandlerGroupManager handlerGroupManager = EzyHandlerGroupManagerImpl.builder()
                .handlerGroupBuilderFactory(handlerGroupBuilderFactory)
                .build();
        return handlerGroupManager;
    }

    public static class ExEzyByteToObjectDecoder implements EzyStringToObjectDecoder {

        @Override
        public Object decode(String bytes) throws Exception {
            return EzyEntityFactory.newArrayBuilder()
                    .append(EzyCommand.PING.getId())
                    .build();
        }

        @Override
        public Object decode(byte[] bytes) throws Exception {
            return EzyEntityFactory.newArrayBuilder()
                    .append(EzyCommand.PING.getId())
                    .build();
        }

    }
}

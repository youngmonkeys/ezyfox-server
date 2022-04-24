package com.tvd12.ezyfoxserver.nio.testing.websocket;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.callback.EzyCallback;
import com.tvd12.ezyfox.codec.EzyStringToObjectDecoder;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyStreamingController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.websocket.EzySimpleWsHandlerGroup;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySimpleWsHandlerGroupTest extends BaseTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        EzySessionTicketsQueue socketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        EzySessionTicketsQueue webSocketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        EzySessionManager sessionManager = EzyNioSessionManagerImpl.builder()
                .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
                .tokenGenerator(new EzySimpleSessionTokenGenerator())
                .build();
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        server.setSessionManager(sessionManager);

        EzyServerControllers controllers = mock(EzyServerControllers.class);
        server.setControllers(controllers);

        EzyInterceptor interceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.PING)).thenReturn(interceptor);

        EzyController controller = mock(EzyController.class);
        when(controllers.getController(EzyCommand.PING)).thenReturn(controller);

        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.WEBSOCKET);
        EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
                .statistics(statistics)
                .serverContext(serverContext)
                .statsThreadPool(statsThreadPool)
                .streamQueue(streamQueue)
                .codecFactory(new ExCodecFactory())
                .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
                .socketSessionTicketsQueue(socketSessionTicketsQueue)
                .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
                .build();

        EzySimpleWsHandlerGroup group = (EzySimpleWsHandlerGroup) handlerGroupBuilderFactory
                .newBuilder(channel, EzyConnectionType.WEBSOCKET)
                .build();

        group.fireBytesReceived("hello");
        group.fireBytesReceived(new byte[] {127, 2, 3, 4, 5, 6}, 0, 5);
        ((EzyAbstractSession)session).setStreamingEnable(true);
        group.fireBytesReceived(new byte[] {127, 2, 3, 4, 5, 6}, 0, 5);
        EzySimplePacket packet = new EzySimplePacket();
        packet.setBinary(false);
        packet.setData("world".getBytes());
        packet.setTransportType(EzyTransportType.TCP);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        group.firePacketSend(packet, writeBuffer);
        group.sendPacketNow(packet);
        group.fireChannelRead(EzyCommand.PING, EzyEntityArrays.newArray(EzyCommand.PING.getId(), EzyEntityFactory.EMPTY_ARRAY));

        EzyInterceptor streamInterceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamInterceptor);

        EzyStreamingController streamController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamController);

        group.fireStreamBytesReceived(new byte[] {0, 1, 2});

        EzyPacket droppedPacket = mock(EzyPacket.class);
        when(droppedPacket.getSize()).thenReturn(12);
        group.addDroppedPacket(droppedPacket);
        EzyPacket failedPacket = mock(EzyPacket.class);
        when(failedPacket.getData()).thenReturn(new byte[] {1, 2, 3});
        when(failedPacket.isBinary()).thenReturn(false);
        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenThrow(new IllegalStateException("maintain"));
        group.firePacketSend(failedPacket, writeBuffer);

        MethodInvoker.create()
            .object(group)
            .method("executeHandleReceivedBytes")
            .param("hello")
            .invoke();

        MethodInvoker.create()
            .object(group)
            .method("executeHandleReceivedBytes")
            .param("hello".getBytes())
            .invoke();

        group.fireChannelInactive();
        Thread.sleep(2000);
        group.destroy();
        group.destroy();

        EzySocketStreamQueue streamQueue1 = mock(EzySocketStreamQueue.class);
        when(streamQueue1.add(any())).thenThrow(new IllegalStateException("queue full"));
        group = (EzySimpleWsHandlerGroup) handlerGroupBuilderFactory
                .newBuilder(channel, EzyConnectionType.WEBSOCKET)
                .session(session)
                .decoder(decoder)
                .serverContext(serverContext)
                .statsThreadPool(statsThreadPool)
                .streamQueue(streamQueue1)
                .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
                .build();
        group.fireBytesReceived(new byte[] {127, 2, 3, 4, 5, 6}, 0, 5);
    }

    @Test
    public void newDecodeBytesCallbackCallTest() throws Exception {
        // given
        EzyWsHandlerGroup sut = newHandlerGroup();

        EzyCallback<Object> decodeBytesCallback = FieldUtil.getFieldValue(sut, "decodeBytesCallback");

        // when
        Throwable e = Asserts.assertThrows(() -> decodeBytesCallback.call(new Object()));

        // then
        Asserts.assertEquals(UnsupportedOperationException.class, e.getClass());
    }

    @Test
    public void handleReceivedBytesLenLowerThan1() throws Exception {
        // given
        EzyWsHandlerGroup sut = newHandlerGroup();

        byte[] bytes = new byte[0];
        int offset = 0;
        int len = 0;

        // when
        // then
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedBytes")
            .param(byte[].class, bytes)
            .param(int.class, offset)
            .param(int.class, len)
            .call();
    }

    @Test
    public void handleReceivedStreamNotEnable() throws Exception {
        // given
        EzyWsHandlerGroup sut = newHandlerGroup(false);

        byte[] bytes = new byte[] {1 << 4, 2, 3};
        int offset = 0;
        int len = 3;

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedBytes")
            .param(byte[].class, bytes)
            .param(int.class, offset)
            .param(int.class, len)
            .call();

        // then
        EzySession session = FieldUtil.getFieldValue(sut, "session");
        verify(session, times(1)).isStreamingEnable();
    }

    @Test
    public void handleReceivedStreamEnableAndSessionStream() throws Exception {
        // given
        EzyWsHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");
        when(session.isStreamingEnable()).thenReturn(true);

        byte[] bytes = new byte[] {1 << 4, 2, 3};
        int offset = 0;
        int len = 3;

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedBytes")
            .param(byte[].class, bytes)
            .param(int.class, offset)
            .param(int.class, len)
            .call();

        // then
        verify(session, times(1)).isStreamingEnable();
    }

    @Test
    public void handleReceivedSessionStreamNotEnable() throws Exception {
        // given
        EzyWsHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");

        byte[] bytes = new byte[] {1 << 4, 2, 3};
        int offset = 0;
        int len = 3;

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedBytes")
            .param(byte[].class, bytes)
            .param(int.class, offset)
            .param(int.class, len)
            .call();

        // then
        verify(session, times(1)).isStreamingEnable();
    }

    @Test
    public void handleReceivedSessionStreamEnable() throws Exception {
        // given
        EzyWsHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");
        when(session.isStreamingEnable()).thenReturn(true);

        byte[] bytes = new byte[] {1 << 4, 2, 3};
        int offset = 0;
        int len = 3;

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedBytes")
            .param(byte[].class, bytes)
            .param(int.class, offset)
            .param(int.class, len)
            .call();

        // then
        verify(session, times(1)).isStreamingEnable();
    }

    private EzySimpleWsHandlerGroup newHandlerGroup() throws IOException {
        return newHandlerGroup(true);
    }

    @SuppressWarnings("rawtypes")
    private EzySimpleWsHandlerGroup newHandlerGroup(boolean streamEnable) throws IOException {
        EzySessionTicketsQueue socketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        EzySessionTicketsQueue webSocketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        EzySessionManager sessionManager = EzyNioSessionManagerImpl.builder()
                .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
                .tokenGenerator(new EzySimpleSessionTokenGenerator())
                .build();
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(streamEnable);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        server.setSessionManager(sessionManager);

        EzyServerControllers controllers = mock(EzyServerControllers.class);
        server.setControllers(controllers);

        EzySimpleConfig config = new EzySimpleConfig();
        server.setConfig(config);

        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.WEBSOCKET);
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        EzySimpleSession session = mock(EzySimpleSession.class);
        when(session.getChannel()).thenReturn(channel);

        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
                .statistics(statistics)
                .serverContext(serverContext)
                .statsThreadPool(statsThreadPool)
                .streamQueue(streamQueue)
                .codecFactory(new ExCodecFactory())
                .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
                .socketSessionTicketsQueue(socketSessionTicketsQueue)
                .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
                .build();

        EzySimpleWsHandlerGroup group = (EzySimpleWsHandlerGroup) handlerGroupBuilderFactory
                .newBuilder(channel, EzyConnectionType.WEBSOCKET)
                .session(session)
                .build();
        return group;
    }

    public static class ExCodecFactory implements EzyCodecFactory {
        @Override
        public Object newDecoder(EzyConstant type) {
            return new ExEzyByteToObjectDecoder();
        }

        @Override
        public Object newEncoder(EzyConstant type) {
            return null;
        }
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

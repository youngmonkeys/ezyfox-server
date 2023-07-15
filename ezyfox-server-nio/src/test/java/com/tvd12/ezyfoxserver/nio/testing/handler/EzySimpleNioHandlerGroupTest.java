package com.tvd12.ezyfoxserver.nio.testing.handler;

import com.tvd12.ezyfox.codec.*;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPackets;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

public class EzySimpleNioHandlerGroupTest extends BaseTest {

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
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
            .statistics(statistics)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .codecFactory(new ExCodecFactory())
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
            .build();

        EzySimpleNioHandlerGroup group = (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .build();

        group.fireBytesReceived("hello".getBytes());
        EzySimplePacket packet = new EzySimplePacket();
        packet.setBinary(true);
        packet.setData("world".getBytes());
        packet.setTransportType(EzyTransportType.TCP);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        group.firePacketSend(packet, writeBuffer);
        group.sendPacketNow(packet);
        group.fireChannelRead(EzyCommand.PING, EzyEntityArrays.newArray(EzyCommand.PING.getId(), EzyEntityFactory.EMPTY_ARRAY));
        group.fireStreamBytesReceived(new byte[]{0, 1, 2});
        EzyPacket droppedPacket = mock(EzyPacket.class);
        when(droppedPacket.getSize()).thenReturn(12);
        group.addDroppedPacket(droppedPacket);
        EzyPacket failedPacket = mock(EzyPacket.class);
        when(failedPacket.getData()).thenReturn(new byte[]{1, 2, 3});
        when(failedPacket.isBinary()).thenReturn(true);
        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenThrow(new IllegalStateException("maintain"));
        group.firePacketSend(failedPacket, writeBuffer);
        group.fireChannelInactive();
        Thread.sleep(2000);
        group.destroy();
        group.destroy();

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        group = (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .session(session)
            .streamQueue(streamQueue)
            .decoder(new ExStreamEzyByteToObjectDecoder())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
        ((EzyAbstractSession) session).setStreamingEnable(false);
        group.fireBytesReceived("hello".getBytes());
        Thread.sleep(500);
        ((EzyAbstractSession) session).setStreamingEnable(true);
        group.fireBytesReceived("hello".getBytes());
        Thread.sleep(1000);

        streamQueue = mock(EzySocketStreamQueue.class);
        when(streamQueue.add(any())).thenThrow(new IllegalStateException("maintain"));
        group = (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .session(session)
            .streamQueue(streamQueue)
            .decoder(new ExStreamEzyByteToObjectDecoder())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
        group.fireBytesReceived("hello".getBytes());
        Thread.sleep(1000);

        group = (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .session(session)
            .decoder(new ErrorEzyByteToObjectDecoder())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
        group.fireBytesReceived("hello".getBytes());
        group.fireMessageReceived(mock(EzyMessage.class));
        Thread.sleep(300);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void writeSuccessTest() throws Exception {
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
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);

        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
            .statistics(statistics)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .codecFactory(new ExCodecFactory())
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
            .build();

        EzySimpleNioHandlerGroup group = (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .session(session)
            .decoder(decoder)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
        EzySimplePacket packet = new EzySimplePacket();
        packet.setBinary(true);
        packet.setData("world".getBytes());
        packet.setTransportType(EzyTransportType.TCP);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        group.firePacketSend(packet, writeBuffer);
        Thread.sleep(1000);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void writeFailureTest() throws Exception {
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
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        SelectionKey selectionKey = mock(SelectionKey.class);
        when(selectionKey.isValid()).thenReturn(true);
        session.setProperty(EzyNioSession.SELECTION_KEY, selectionKey);

        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
            .statistics(statistics)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .codecFactory(new ExCodecFactory())
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
            .build();

        EzySimpleNioHandlerGroup group = (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .session(session)
            .decoder(decoder)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
        EzySimplePacket packet = new EzySimplePacket();
        packet.setBinary(true);
        packet.setData("world".getBytes());
        packet.setTransportType(EzyTransportType.TCP);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        group.firePacketSend(packet, writeBuffer);
        Thread.sleep(1000);
    }

    @Test
    public void handleReceivedMessageHeaderIsNotRawBytes() throws Exception {
        // given
        EzySimpleNioHandlerGroup sut = newHandlerGroup();

        EzyMessage message = mock(EzyMessage.class);
        EzyMessageHeader header = mock(EzyMessageHeader.class);
        when(message.getHeader()).thenReturn(header);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedMessage")
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(header, times(1)).isRawBytes();

        EzySimpleMessageDataDecoder decoder = FieldUtil.getFieldValue(sut, "decoder");
        EzyByteToObjectDecoder d = FieldUtil.getFieldValue(decoder, "decoder");
        EzySession session = FieldUtil.getFieldValue(sut, "session");
        verify(d, times(1)).decode(message, session.getSessionKey());
    }

    @Test
    public void handleReceivedMessageStreamingNotEnable() throws Exception {
        // given
        EzySimpleNioHandlerGroup sut = newHandlerGroup(false);

        EzyMessage message = mock(EzyMessage.class);
        EzyMessageHeader header = mock(EzyMessageHeader.class);
        when(header.isRawBytes()).thenReturn(true);
        when(message.getHeader()).thenReturn(header);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedMessage")
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(header, times(1)).isRawBytes();
    }

    @Test
    public void handleReceivedMessageSessionStreamingNotEnable() throws Exception {
        // given
        EzySimpleNioHandlerGroup sut = newHandlerGroup();

        EzyMessage message = mock(EzyMessage.class);
        EzyMessageHeader header = mock(EzyMessageHeader.class);
        when(header.isRawBytes()).thenReturn(true);
        when(message.getHeader()).thenReturn(header);

        EzySession session = FieldUtil.getFieldValue(sut, "session");
        when(session.isStreamingEnable()).thenReturn(false);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedMessage")
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(header, times(1)).isRawBytes();
        verify(session, times(1)).isStreamingEnable();
    }

    @Test
    public void writePacketToSocketSessionKeyIsInvalid() throws Exception {
        // given
        EzySimpleNioHandlerGroup sut = newHandlerGroup();

        EzyNioSession session = FieldUtil.getFieldValue(sut, "session");
        SelectionKey selectionKey = mock(SelectionKey.class);
        when(session.getSelectionKey()).thenReturn(selectionKey);
        when(selectionKey.isValid()).thenReturn(false);

        EzyPacket packet = mock(EzyPacket.class);
        when(packet.getData()).thenReturn(new byte[]{1, 2, 3, 5});
        when(packet.isBinary()).thenReturn(true);

        ByteBuffer writeBuffer = ByteBuffer.allocate(5);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("writePacketToSocket")
            .param(EzyPacket.class, packet)
            .param(Object.class, writeBuffer)
            .call();

        // then
        verify(packet, times(1)).getData();
        verify(packet, times(1)).isBinary();
        verify(packet, times(1)).setFragment(any());
        verifyNoMoreInteractions(packet);

        verify(selectionKey, times(1)).isValid();
        verifyNoMoreInteractions(selectionKey);

        verify(session, times(1)).getSelectionKey();
        verify(session, times(2)).getChannel();
        verify((EzyAbstractSession) session, times(1))
                .setDelegate(any(EzySessionDelegate.class));
        verify((EzyAbstractSession) session, times(1))
                .setDisconnectionQueue(any(EzySocketDisconnectionQueue.class));
        verify((EzyAbstractSession) session, times(1))
                .setSessionTicketsQueue(any(EzySessionTicketsQueue.class));
        verify((EzyAbstractSession) session, times(1))
                .setDroppedPackets(any(EzyDroppedPackets.class));
        verify((EzyAbstractSession) session, times(1))
                .setImmediateDeliver(any(EzyImmediateDeliver.class));
        verifyNoMoreInteractions(session);

    }

    @Test
    public void writePacketToSocketSessionKeyIsValid() throws Exception {
        // given
        EzySimpleNioHandlerGroup sut = newHandlerGroup();

        EzyNioSession session = FieldUtil.getFieldValue(sut, "session");
        SelectionKey selectionKey = mock(SelectionKey.class);
        when(session.getSelectionKey()).thenReturn(selectionKey);
        when(selectionKey.isValid()).thenReturn(true);

        EzyPacket packet = mock(EzyPacket.class);
        when(packet.getData()).thenReturn(new byte[]{1, 2, 3, 5});
        when(packet.isBinary()).thenReturn(true);

        ByteBuffer writeBuffer = ByteBuffer.allocate(5);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("writePacketToSocket")
            .param(EzyPacket.class, packet)
            .param(Object.class, writeBuffer)
            .call();

        // then
        verify(packet, times(1)).getData();
        verify(packet, times(1)).isBinary();
        verify(packet, times(1)).setFragment(any());
        verifyNoMoreInteractions(packet);

        verify(selectionKey, times(1)).isValid();
        verify(selectionKey, times(1)).interestOps(
            SelectionKey.OP_READ | SelectionKey.OP_WRITE
        );
        verifyNoMoreInteractions(selectionKey);

        verify(session, times(1)).getSelectionKey();
        verify(session, times(2)).getChannel();
        verify((EzyAbstractSession) session, times(1))
            .setDelegate(any(EzySessionDelegate.class));
        verify((EzyAbstractSession) session, times(1))
            .setDisconnectionQueue(any(EzySocketDisconnectionQueue.class));
        verify((EzyAbstractSession) session, times(1))
            .setSessionTicketsQueue(any(EzySessionTicketsQueue.class));
        verify((EzyAbstractSession) session, times(1))
            .setDroppedPackets(any(EzyDroppedPackets.class));
        verify((EzyAbstractSession) session, times(1))
            .setImmediateDeliver(any(EzyImmediateDeliver.class));
        verifyNoMoreInteractions(session);

    }

    @Test
    public void writePacketToSocketNormally() throws Exception {
        // given
        EzySimpleNioHandlerGroup sut = newHandlerGroup();

        EzyNioSession session = FieldUtil.getFieldValue(sut, "session");
        SelectionKey selectionKey = mock(SelectionKey.class);
        when(session.getSelectionKey()).thenReturn(selectionKey);
        when(selectionKey.isValid()).thenReturn(false);

        EzyPacket packet = mock(EzyPacket.class);
        byte[] data = new byte[] {1, 2, 3, 5};
        when(packet.getData()).thenReturn(data);
        when(packet.isBinary()).thenReturn(true);

        EzyChannel channel = session.getChannel();
        when(channel.write(any(), anyBoolean()))
            .thenReturn(data.length);

        ByteBuffer writeBuffer = ByteBuffer.allocate(5);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("writePacketToSocket")
            .param(EzyPacket.class, packet)
            .param(Object.class, writeBuffer)
            .call();

        // then
        verify(packet, times(1)).getData();
        verify(packet, times(1)).isBinary();
        verify(packet, times(1)).release();
        verifyNoMoreInteractions(packet);

        verify(channel, times(1)).write(
            any(),
            anyBoolean()
        );
        verify(channel, times(1)).getConnectionType();
        verify(channel, times(1)).getClientAddress();
        verify(channel, times(1)).getConnection();
        verifyNoMoreInteractions(channel);

        verify(session, times(3)).getChannel();
        verify((EzyAbstractSession) session, times(1))
            .setDelegate(any(EzySessionDelegate.class));
        verify((EzyAbstractSession) session, times(1))
            .setDisconnectionQueue(any(EzySocketDisconnectionQueue.class));
        verify((EzyAbstractSession) session, times(1))
            .setSessionTicketsQueue(any(EzySessionTicketsQueue.class));
        verify((EzyAbstractSession) session, times(1))
            .setDroppedPackets(any(EzyDroppedPackets.class));
        verify((EzyAbstractSession) session, times(1))
            .setImmediateDeliver(any(EzyImmediateDeliver.class));
        verifyNoMoreInteractions(session);

    }

    private EzySimpleNioHandlerGroup newHandlerGroup() throws IOException {
        return newHandlerGroup(true);
    }

    @SuppressWarnings("rawtypes")
    private EzySimpleNioHandlerGroup newHandlerGroup(boolean streamEnable) throws IOException {
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
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        EzySimpleSession session = mock(EzySimpleSession.class);
        when(session.getChannel()).thenReturn(channel);

        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        SelectionKey selectionKey = mock(SelectionKey.class);
        when(selectionKey.isValid()).thenReturn(true);
        when(session.getProperty(EzyNioSession.SELECTION_KEY)).thenReturn(selectionKey);

        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        EzyByteToObjectDecoder decoder = mock(EzyByteToObjectDecoder.class);
        when(codecFactory.newDecoder(any())).thenReturn(decoder);

        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
            .statistics(statistics)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .codecFactory(codecFactory)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
            .build();

        return (EzySimpleNioHandlerGroup) handlerGroupBuilderFactory
            .newBuilder(channel, EzyConnectionType.SOCKET)
            .session(session)
            .decoder(decoder)
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
    }

    public static class ExEzyByteToObjectDecoder implements EzyByteToObjectDecoder {

        @Override
        public void reset() {}

        @Override
        public Object decode(EzyMessage message) {
            return EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.PING.getId())
                .build();
        }

        @Override
        public void decode(ByteBuffer bytes, Queue<EzyMessage> queue) {
            EzyMessageHeader header = new EzySimpleMessageHeader(false, false, false, false, false, false);
            byte[] content = new byte[bytes.remaining()];
            bytes.get(content);
            EzyMessage message = new EzySimpleMessage(header, content, content.length);
            queue.add(message);
        }
    }

    @Test
    public void builderTest() {
        // given
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);

        @SuppressWarnings("rawtypes")
        EzySessionManager sessionManager = EzyNioSessionManagerImpl.builder()
            .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
            .tokenGenerator(new EzySimpleSessionTokenGenerator())
            .build();
        server.setSessionManager(sessionManager);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzySessionTicketsQueue sessionTicketsQueue =
            mock(EzySessionTicketsQueue.class);
        EzySocketDisconnectionQueue disconnectionQueue =
            mock(EzySocketDisconnectionQueue.class);

        Object connection = new Object();
        EzyChannel channel = mock(EzyChannel.class);
        when(channel.getConnection()).thenReturn(connection);
        EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);

        EzySessionStats sessionStats = mock(EzySessionStats.class);
        doNothing().when(sessionStats).setCurrentSessions(anyInt());

        // when
        EzySimpleNioHandlerGroup sut = (EzySimpleNioHandlerGroup) EzySimpleNioHandlerGroup.builder()
            .session(session)
            .serverContext(serverContext)
            .sessionCount(new AtomicInteger())
            .sessionStats(sessionStats)
            .sessionTicketsQueue(sessionTicketsQueue)
            .disconnectionQueue(disconnectionQueue)
            .build();

        // then
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "disconnectionQueue"),
            disconnectionQueue
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "sessionTicketsQueue"),
            sessionTicketsQueue
        );
    }

    public static class ExStreamEzyByteToObjectDecoder implements EzyByteToObjectDecoder {

        @Override
        public void reset() {}

        @Override
        public Object decode(EzyMessage message) {
            return EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.PING.getId())
                .build();
        }

        @Override
        public void decode(ByteBuffer bytes, Queue<EzyMessage> queue) {
            EzyMessageHeader header = new EzySimpleMessageHeader(true, true, true, true, true, false);
            byte[] content = new byte[bytes.remaining()];
            bytes.get(content);
            EzyMessage message = new EzySimpleMessage(header, content, content.length);
            queue.add(message);
        }

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

    public static class ErrorEzyByteToObjectDecoder implements EzyByteToObjectDecoder {

        @Override
        public void reset() {}

        @Override
        public Object decode(EzyMessage message) {
            throw new IllegalStateException("maintain");
        }

        @Override
        public void decode(ByteBuffer bytes, Queue<EzyMessage> queue) {
            throw new IllegalStateException("maintain");
        }
    }
}

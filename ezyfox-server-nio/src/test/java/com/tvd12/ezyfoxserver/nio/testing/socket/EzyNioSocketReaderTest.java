package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfox.codec.*;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketChannel;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketReader;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
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
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzyNioSocketReaderTest extends BaseTest {

    @Test
    public void test() throws Exception {
        EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager();
        EzySocketDataReceiver socketDataReceiver = EzySocketDataReceiver.builder()
            .threadPoolSize(1)
            .handlerGroupManager(handlerGroupManager)
            .build();

        Selector ownSelector = spy(ExSelector.class);
        when(ownSelector.selectNow()).thenReturn(1);
        SelectionKey selectionKey1 = spy(ExSelectionKey.class);
        SelectionKey selectionKey2 = spy(ExSelectionKey.class);
        SelectionKey selectionKey3 = spy(ExSelectionKey.class);
        SelectionKey selectionKey4 = spy(ExSelectionKey.class);
        SelectionKey selectionKey5 = spy(ExSelectionKey.class);
        when(ownSelector.selectedKeys()).thenReturn(Sets.newHashSet(
            selectionKey1,
            selectionKey2,
            selectionKey3, selectionKey4, selectionKey5));
        when(selectionKey1.isValid()).thenReturn(true);
        when(selectionKey1.readyOps()).thenReturn(SelectionKey.OP_READ);
        when(selectionKey2.isValid()).thenReturn(true);
        when(selectionKey2.readyOps()).thenReturn(SelectionKey.OP_WRITE);
        when(selectionKey3.isValid()).thenReturn(false);
        when(selectionKey4.isValid()).thenReturn(true);
        when(selectionKey4.readyOps()).thenReturn(SelectionKey.OP_READ);
        when(selectionKey5.isValid()).thenReturn(true);
        when(selectionKey5.readyOps()).thenReturn(SelectionKey.OP_READ);

        SocketChannel socketChannel1 = mock(SocketChannel.class);
        EzyChannel channel1 = new EzyNioSocketChannel(socketChannel1);

        handlerGroupManager.newHandlerGroup(channel1, EzyConnectionType.SOCKET);

        when(selectionKey1.channel()).thenReturn(socketChannel1);
        when(socketChannel1.isConnected()).thenReturn(true);
        when(socketChannel1.read(any(ByteBuffer.class))).then((Answer<Integer>) invocation -> {
            ByteBuffer buffer = invocation.getArgumentAt(0, ByteBuffer.class);
            buffer.put("hello".getBytes());
            return "hello".length();
        });

        SocketChannel socketChannel4 = mock(SocketChannel.class);
        when(selectionKey4.channel()).thenReturn(socketChannel4);

        SocketChannel socketChannel5 = spy(ExSocketChannel.class);
        EzyChannel channel5 = new EzyNioSocketChannel(socketChannel5);
        when(selectionKey5.channel()).thenReturn(socketChannel5);
        doNothing().when(socketChannel5).close();

        handlerGroupManager.newHandlerGroup(channel5, EzyConnectionType.SOCKET);

        when(selectionKey5.channel()).thenReturn(socketChannel5);
        when(socketChannel5.isConnected()).thenReturn(true);
        when(socketChannel5.read(any(ByteBuffer.class))).then((Answer<Integer>) invocation -> -1);

        EzyNioSocketAcceptor socketAcceptor = new EzyNioSocketAcceptor();
        socketAcceptor.setReadSelector(ownSelector);
        socketAcceptor.setHandlerGroupManager(handlerGroupManager);

        EzyNioSocketReader socketReader = new EzyNioSocketReader();
        socketReader.setOwnSelector(ownSelector);
        socketReader.setAcceptableConnectionsHandler(socketAcceptor);
        socketReader.setSocketDataReceiver(socketDataReceiver);
        socketReader.handleEvent();
    }

    @Test
    public void testExceptionCase() throws Exception {
        EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager();
        EzySocketDataReceiver socketDataReceiver = EzySocketDataReceiver.builder()
            .threadPoolSize(1)
            .handlerGroupManager(handlerGroupManager)
            .build();

        Selector ownSelector = spy(ExSelector.class);
        when(ownSelector.selectNow()).thenReturn(1);
        SelectionKey selectionKey1 = spy(ExSelectionKey.class);
        when(ownSelector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey1));
        when(selectionKey1.isValid()).thenReturn(true);
        when(selectionKey1.readyOps()).thenReturn(SelectionKey.OP_READ);

        SocketChannel socketChannel1 = mock(SocketChannel.class);
        EzyChannel channel1 = new EzyNioSocketChannel(socketChannel1);

        handlerGroupManager.newHandlerGroup(channel1, EzyConnectionType.SOCKET);

        when(selectionKey1.channel()).thenReturn(socketChannel1);
        when(socketChannel1.isConnected()).thenReturn(true);
        when(socketChannel1.read(any(ByteBuffer.class))).then((Answer<Integer>) invocation -> {
            throw new IllegalStateException("server maintain");
        });

        EzyNioSocketAcceptor socketAcceptor = new EzyNioSocketAcceptor();
        socketAcceptor.setReadSelector(ownSelector);
        socketAcceptor.setHandlerGroupManager(handlerGroupManager);

        EzyNioSocketReader socketReader = new EzyNioSocketReader();
        socketReader.setOwnSelector(ownSelector);
        socketReader.setAcceptableConnectionsHandler(socketAcceptor);
        socketReader.setSocketDataReceiver(socketDataReceiver);
        socketReader.handleEvent();
    }

    private EzyHandlerGroupManager newHandlerGroupManager() {
        EzyNioSessionManager sessionManager = (EzyNioSessionManager) EzyNioSessionManagerImpl.builder()
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
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        return EzyHandlerGroupManagerImpl.builder()
            .handlerGroupBuilderFactory(handlerGroupBuilderFactory)
            .build();
    }

    public static abstract class ExSocketChannel extends SocketChannel {

        public ExSocketChannel() {
            super(SelectorProvider.provider());
        }

    }

    public static abstract class ExSelector extends Selector {
        public ExSelector() {}
    }

    public static abstract class ExSelectionKey extends SelectionKey {
        public ExSelectionKey() {}
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
}

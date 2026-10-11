package com.tvd12.ezyfoxserver.nio.testing;

import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.creator.EzySessionCreator;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.protocol.EzyProtocolHandlerGroup;
import com.tvd12.ezyfoxserver.nio.websocket.EzySimpleWsHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

public class EzyHandlerGroupBuilderFactoryImplTest {

    private EzyStatistics statistics;
    private ExecutorService statsThreadPool;
    private EzyCodecFactory codecFactory;
    private EzyServerContext serverContext;
    private EzySessionCreator sessionCreator;
    private EzySocketStreamQueue streamQueue;
    private EzySocketDisconnectionQueue disconnectionQueue;
    private EzySessionTicketsQueue socketSessionTicketsQueue;
    private EzySessionTicketsQueue webSocketSessionTicketsQueue;
    private EzySessionTicketsQueue protocolSessionTicketsQueue;
    private EzySessionTicketsRequestQueues sessionTicketsRequestQueues;
    private EzyChannel channel;
    private EzyNioSession session;
    private EzyHandlerGroupBuilderFactory factory;

    @BeforeMethod
    public void setup() {
        statistics = new EzySimpleStatistics();
        statsThreadPool = mock(ExecutorService.class);
        codecFactory = mock(EzyCodecFactory.class);
        serverContext = mock(EzyServerContext.class);
        sessionCreator = mock(EzySessionCreator.class);
        streamQueue = mock(EzySocketStreamQueue.class);
        disconnectionQueue = mock(EzySocketDisconnectionQueue.class);
        socketSessionTicketsQueue = mock(EzySessionTicketsQueue.class);
        webSocketSessionTicketsQueue = mock(EzySessionTicketsQueue.class);
        protocolSessionTicketsQueue = mock(EzySessionTicketsQueue.class);
        sessionTicketsRequestQueues = mock(EzySessionTicketsRequestQueues.class);

        channel = mock(EzyChannel.class);
        session = mock(EzyNioSession.class);
        when(sessionCreator.create(channel)).thenReturn(session);

        factory = EzyHandlerGroupBuilderFactoryImpl
            .builder()
            .statistics(statistics)
            .statsThreadPool(statsThreadPool)
            .codecFactory(codecFactory)
            .serverContext(serverContext)
            .sessionCreator(sessionCreator)
            .streamQueue(streamQueue)
            .disconnectionQueue(disconnectionQueue)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
            .protocolSessionTicketsQueue(protocolSessionTicketsQueue)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
    }

    @Test
    public void buildWithSessionCreatorNotNull() {
        // given
        EzySessionCreator sessionCreator = mock(EzySessionCreator.class);
        EzyHandlerGroupBuilderFactoryImpl.Builder builder =
            EzyHandlerGroupBuilderFactoryImpl.builder()
                .sessionCreator(sessionCreator);

        // when
        EzyHandlerGroupBuilderFactory factor = builder.build();

        // then
        Asserts.assertNotNull(factor);
    }

    @Test
    public void newBuilderBySocketTypeTest() {
        Object decoder = new Object();
        when(codecFactory.newDecoder(EzyConnectionType.SOCKET)).thenReturn(decoder);

        EzyAbstractHandlerGroup.Builder builder =
            factory.newBuilder(channel, EzyConnectionType.SOCKET.getName());

        Asserts.assertEqualsType(builder, EzySimpleNioHandlerGroup.Builder.class);
        assert FieldUtil.getFieldValue(builder, "sessionCount")
            == FieldUtil.getFieldValue(factory, "socketSessionCount");
        assert FieldUtil.getFieldValue(builder, "sessionStats")
            == statistics.getSocketStats().getSessionStats();
        assert FieldUtil.getFieldValue(builder, "networkStats")
            == statistics.getSocketStats().getNetworkStats();
        assert FieldUtil.getFieldValue(builder, "sessionTicketsQueue")
            == socketSessionTicketsQueue;
        assert FieldUtil.getFieldValue(builder, "decoder") == decoder;

        verify(codecFactory, times(1)).newDecoder(EzyConnectionType.SOCKET);
        verifyNoMoreInteractions(codecFactory);
    }

    @Test
    public void newBuilderByWebSocketTypeTest() {
        Object decoder = new Object();
        when(codecFactory.newDecoder(EzyConnectionType.WEBSOCKET)).thenReturn(decoder);

        EzyAbstractHandlerGroup.Builder builder =
            factory.newBuilder(channel, EzyConnectionType.WEBSOCKET.getName());

        Asserts.assertEqualsType(builder, EzySimpleWsHandlerGroup.Builder.class);
        assert FieldUtil.getFieldValue(builder, "sessionCount")
            == FieldUtil.getFieldValue(factory, "webSocketSessionCount");
        assert FieldUtil.getFieldValue(builder, "sessionStats")
            == statistics.getWebSocketStats().getSessionStats();
        assert FieldUtil.getFieldValue(builder, "networkStats")
            == statistics.getWebSocketStats().getNetworkStats();
        assert FieldUtil.getFieldValue(builder, "sessionTicketsQueue")
            == webSocketSessionTicketsQueue;
        assert FieldUtil.getFieldValue(builder, "decoder") == decoder;

        verify(codecFactory, times(1)).newDecoder(EzyConnectionType.WEBSOCKET);
        verifyNoMoreInteractions(codecFactory);
    }

    @Test
    public void newBuilderByProtocolTypeTest() {
        EzyAbstractHandlerGroup.Builder builder =
            factory.newBuilder(channel, "test-protocol");

        Asserts.assertEqualsType(builder, EzyProtocolHandlerGroup.Builder.class);
        assert FieldUtil.getFieldValue(builder, "sessionCount")
            == FieldUtil.getFieldValue(factory, "socketSessionCount");
        assert FieldUtil.getFieldValue(builder, "sessionStats")
            == statistics.getSocketStats().getSessionStats();
        assert FieldUtil.getFieldValue(builder, "networkStats")
            == statistics.getSocketStats().getNetworkStats();
        assert FieldUtil.getFieldValue(builder, "sessionTicketsQueue")
            == protocolSessionTicketsQueue;
        assert FieldUtil.getFieldValue(builder, "session") == session;
        assert FieldUtil.getFieldValue(builder, "serverContext") == serverContext;
        assert FieldUtil.getFieldValue(builder, "streamQueue") == streamQueue;
        assert FieldUtil.getFieldValue(builder, "disconnectionQueue")
            == disconnectionQueue;
        assert FieldUtil.getFieldValue(builder, "statsThreadPool") == statsThreadPool;
        assert FieldUtil.getFieldValue(builder, "sessionTicketsRequestQueues")
            == sessionTicketsRequestQueues;
        Asserts.assertNull(FieldUtil.getFieldValue(builder, "decoder"));

        verify(sessionCreator, times(1)).create(channel);
        verifyZeroInteractions(codecFactory);
    }

    @Test
    public void newBuilderByNullTypeTest() {
        EzyAbstractHandlerGroup.Builder builder = factory.newBuilder(channel, (String) null);

        Asserts.assertEqualsType(builder, EzyProtocolHandlerGroup.Builder.class);
        assert FieldUtil.getFieldValue(builder, "sessionTicketsQueue")
            == protocolSessionTicketsQueue;
        Asserts.assertNull(FieldUtil.getFieldValue(builder, "decoder"));
        verifyZeroInteractions(codecFactory);
    }
}

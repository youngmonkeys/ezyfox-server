package com.tvd12.ezyfoxserver.nio.testing.protocol;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioDataHandler;
import com.tvd12.ezyfoxserver.nio.protocol.EzyProtocolHandlerGroup;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class EzyProtocolHandlerGroupTest {

    private EzySimpleSession session;
    private EzyNetworkStats networkStats;
    private ExecutorService statsThreadPool;
    private EzySessionTicketsRequestQueues sessionTicketsRequestQueues;
    private EzyProtocolHandlerGroup instance;

    @BeforeMethod
    public void setup() {
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();
        server.setSessionManager(mock(EzySessionManager.class));
        server.setResponseApi(mock(EzyResponseApi.class));

        session = mock(EzySimpleSession.class);
        networkStats = mock(EzyNetworkStats.class);
        statsThreadPool = mock(ExecutorService.class);
        sessionTicketsRequestQueues = mock(EzySessionTicketsRequestQueues.class);
        doAnswer(invocation -> {
            Runnable task = (Runnable) invocation.getArguments()[0];
            task.run();
            return null;
        }).when(statsThreadPool).execute(any(Runnable.class));

        instance = (EzyProtocolHandlerGroup) EzyProtocolHandlerGroup.builder()
            .session(session)
            .sessionCount(new AtomicInteger())
            .sessionStats(mock(EzySessionStats.class))
            .networkStats(networkStats)
            .statsThreadPool(statsThreadPool)
            .serverContext(serverContext)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
    }

    @Test
    public void buildTest() {
        Asserts.assertEqualsType(instance, EzyProtocolHandlerGroup.class);
        Asserts.assertEquals(instance.getSession(), session);
    }

    @Test
    public void newDecoderTest() throws Exception {
        EzyDestroyable decoder = MethodInvoker.create()
            .object(instance)
            .method("newDecoder")
            .param(Object.class, new Object())
            .invoke(EzyDestroyable.class);

        Asserts.assertNotNull(decoder);
        decoder.destroy();
        Asserts.assertNotNull(FieldUtil.getFieldValue(instance, "decoder"));
    }

    @Test
    public void fireDataReceivedTest() {
        when(session.isActivated()).thenReturn(true);
        when(sessionTicketsRequestQueues.addRequest(any(EzySocketRequest.class)))
            .thenReturn(true);
        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append(EzyCommand.APP_REQUEST.getId())
            .append(EzyEntityFactory.newArray())
            .build();

        instance.fireDataReceived(data, 100);

        ArgumentCaptor<EzySocketRequest> requestCaptor =
            ArgumentCaptor.forClass(EzySocketRequest.class);
        verify(sessionTicketsRequestQueues, times(1))
            .addRequest(requestCaptor.capture());
        EzySocketRequest request = requestCaptor.getValue();
        Asserts.assertEquals(request.getSession(), session);
        Asserts.assertEquals(request.getData(), data);
        Asserts.assertEquals(request.getCommand(), EzyCommand.APP_REQUEST);

        verify(session, times(1)).addReceivedRequests(1);
        verify(statsThreadPool, times(1)).execute(any(Runnable.class));
        verify(session, times(1)).addReadBytes(100);
        verify(networkStats, times(1)).addReadBytes(100);
        verify(networkStats, times(1)).addReadPackets(1);
        verify(networkStats, never()).addDroppedInPackets(anyInt());
    }

    @Test
    public void fireDataReceivedWithRequestQueueFullTest() {
        when(session.isActivated()).thenReturn(true);
        when(sessionTicketsRequestQueues.addRequest(any(EzySocketRequest.class)))
            .thenReturn(false);
        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append(EzyCommand.APP_REQUEST.getId())
            .append(EzyEntityFactory.newArray())
            .build();

        instance.fireDataReceived(data, 100);

        verify(sessionTicketsRequestQueues, times(1))
            .addRequest(any(EzySocketRequest.class));
        verify(networkStats, times(1)).addDroppedInPackets(1);
        verify(networkStats, times(1)).addDroppedInBytes(100);
        verify(session, times(1)).addReadBytes(100);
        verify(networkStats, times(1)).addReadBytes(100);
        verify(networkStats, times(1)).addReadPackets(1);
    }

    @Test
    public void fireDataReceivedWithExceptionTest() throws Exception {
        RuntimeException exception = new RuntimeException("test");
        when(session.addReceivedRequests(1)).thenThrow(exception);
        EzyNioDataHandler dataHandler = mock(EzyNioDataHandler.class);
        FieldUtil.setFieldValue(instance, "handler", dataHandler);
        EzyArray data = EzyEntityFactory.newArray();

        instance.fireDataReceived(data, 100);

        verify(dataHandler, times(1)).exceptionCaught(exception);
        verifyZeroInteractions(sessionTicketsRequestQueues);
        verify(statsThreadPool, times(1)).execute(any(Runnable.class));
        verify(session, times(1)).addReadBytes(100);
        verify(networkStats, times(1)).addReadBytes(100);
        verify(networkStats, times(1)).addReadPackets(1);
    }

    @Test
    public void destroyTest() {
        EzyNioDataHandler dataHandler = mock(EzyNioDataHandler.class);
        FieldUtil.setFieldValue(instance, "handler", dataHandler);

        instance.destroy();

        verify(dataHandler, times(1)).destroy();
    }
}

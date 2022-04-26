package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.*;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzySocketStreamHandlerTest {

    @Test
    public void test() {
        EzySocketStreamHandler handler = new EzySocketStreamHandler();

        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        EzySocketDataHandlerGroup dataHandlerGroup = mock(EzySocketDataHandlerGroup.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(dataHandlerGroup);

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketStream stream = new EzySimpleSocketStream(session, new byte[0]);
        streamQueue.add(stream);

        handler.setStreamQueue(streamQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
        handler.destroy();
    }

    @Test
    public void hasNoHandlerGroupCaseTest() {
        EzySocketStreamHandler handler = new EzySocketStreamHandler();

        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(null);

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketStream stream = new EzySimpleSocketStream(session, new byte[0]);
        streamQueue.add(stream);

        handler.setStreamQueue(streamQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
    }

    @Test
    public void doProcessStreamQueueExceptionCaseTest() {
        EzySocketStreamHandler handler = new EzySocketStreamHandler();

        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenThrow(new IllegalArgumentException());

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketStream stream = new EzySimpleSocketStream(session, new byte[0]);
        streamQueue.add(stream);

        handler.setStreamQueue(streamQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
    }

    @Test
    public void doProcessStreamQueueExceptionInterruptTest() throws Exception {
        EzySocketStreamHandler handler = new EzySocketStreamHandler();

        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        EzySocketDataHandlerGroup dataHandlerGroup = mock(EzySocketDataHandlerGroup.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(dataHandlerGroup);

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();

        handler.setStreamQueue(streamQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);

        Thread thread = new Thread(handler::handleEvent);
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }
}

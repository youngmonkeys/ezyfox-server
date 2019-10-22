package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketStream;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupFetcher;
import com.tvd12.ezyfoxserver.socket.EzySocketStream;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
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
        EzySocketDataHandlerGroup dataHandlerGroup = null;
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(dataHandlerGroup);

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketStream stream = new EzySimpleSocketStream(session, new byte[0]);
        streamQueue.add(stream);
        
        handler.setStreamQueue(streamQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
    }
    
    @Test
    public void processStreamQueue0ExceptionCaseTest() {
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
    public void processStreamQueue0ExceptionInterruptTest() throws Exception {
        EzySocketStreamHandler handler = new EzySocketStreamHandler();
        
        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        EzySocketDataHandlerGroup dataHandlerGroup = mock(EzySocketDataHandlerGroup.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(dataHandlerGroup);

        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        
        handler.setStreamQueue(streamQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        
        Thread thread = new Thread(() -> handler.handleEvent());
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }
    
}

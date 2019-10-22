package com.tvd12.ezyfoxserver.testing.socket;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupFetcher;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;

public class EzySocketRequestHandlerTest {

    @Test
    public void test() {
        ExEzySocketRequestHandler handler = new ExEzySocketRequestHandler();
        EzySocketRequestQueues requestQueues = new EzySimpleSocketRequestQueues();
        EzySocketRequestQueue requestQueue = requestQueues.getExtensionQueue();
        
        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        EzySocketDataHandlerGroup handlerGroup = mock(EzySocketDataHandlerGroup.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(handlerGroup);
        
        EzySession session = spy(EzyAbstractSession.class);
        EzyArray array = EzyEntityFactory.newArrayBuilder()
                .append(10)
                .build();
        EzySocketRequest request = new EzySimpleSocketRequest(session, array);
        requestQueue.add(request);
        
        handler.setRequestQueue(requestQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
        handler.destroy();
    }
    
    @Test
    public void handleGroupNullCaseTest() {
        ExEzySocketRequestHandler handler = new ExEzySocketRequestHandler();
        EzySocketRequestQueues requestQueues = new EzySimpleSocketRequestQueues();
        EzySocketRequestQueue requestQueue = requestQueues.getExtensionQueue();
        
        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        EzySocketDataHandlerGroup handlerGroup = null;
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenReturn(handlerGroup);
        
        EzySession session = spy(EzyAbstractSession.class);
        EzyArray array = EzyEntityFactory.newArrayBuilder()
                .append(10)
                .build();
        EzySocketRequest request = new EzySimpleSocketRequest(session, array);
        requestQueue.add(request);
        
        handler.setRequestQueue(requestQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
    }
    
    @Test
    public void processRequestQueue0ExceptionCaseTest() {
        ExEzySocketRequestHandler handler = new ExEzySocketRequestHandler();
        EzySocketRequestQueues requestQueues = new EzySimpleSocketRequestQueues();
        EzySocketRequestQueue requestQueue = requestQueues.getExtensionQueue();
        
        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenThrow(new IllegalArgumentException());
        
        EzySession session = spy(EzyAbstractSession.class);
        EzyArray array = EzyEntityFactory.newArrayBuilder()
                .append(10)
                .build();
        EzySocketRequest request = new EzySimpleSocketRequest(session, array);
        requestQueue.add(request);
        
        handler.setRequestQueue(requestQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        handler.handleEvent();
    }
    
    @Test
    public void processRequestQueue0InterrupTest() throws Exception {
        ExEzySocketRequestHandler handler = new ExEzySocketRequestHandler();
        EzySocketRequestQueues requestQueues = new EzySimpleSocketRequestQueues();
        EzySocketRequestQueue requestQueue = requestQueues.getExtensionQueue();
        
        EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher = mock(EzySocketDataHandlerGroupFetcher.class);
        when(dataHandlerGroupFetcher.getDataHandlerGroup(any(EzySession.class))).thenThrow(new IllegalArgumentException());
        
        handler.setRequestQueue(requestQueue);
        handler.setDataHandlerGroupFetcher(dataHandlerGroupFetcher);
        
        Thread thread = new Thread(() -> handler.handleEvent());
        thread.start();
        Thread.sleep(100L);
        thread.interrupt();
    }
    
    public static class ExEzySocketRequestHandler extends EzySocketRequestHandler {

        @Override
        protected String getRequestType() {
            return "test";
        }
        
    }
    
}

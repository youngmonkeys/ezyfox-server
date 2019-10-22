package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupRemover;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import static org.mockito.Mockito.*;

public class EzySocketDisconnectionHandlerTest {

    @Test
    public void test() {
        EzySocketDisconnectionHandler handler = new EzySocketDisconnectionHandler();
        
        EzySocketDisconnectionQueue disconnectionQueue = EzyBlockingSocketDisconnectionQueue.getInstance();
        
        EzySocketDataHandlerGroupRemover dataHandlerGroupRemover = mock(EzySocketDataHandlerGroupRemover.class);
        EzySocketDataHandlerGroup handlerGroup = mock(EzySocketDataHandlerGroup.class);
        when(dataHandlerGroupRemover.removeHandlerGroup(any(EzySession.class))).thenReturn(handlerGroup);
        
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketDisconnection disconnection = new EzySimpleSocketDisconnection(session);
        disconnectionQueue.add(disconnection);
        
        handler.setDisconnectionQueue(disconnectionQueue);
        handler.setDataHandlerGroupRemover(dataHandlerGroupRemover);
        handler.handleEvent();
        handler.destroy();
    }
    
    @Test
    public void hasNoHandlerGroupCaseTest() {
        EzySocketDisconnectionHandler handler = new EzySocketDisconnectionHandler();
        
        EzySocketDisconnectionQueue disconnectionQueue = EzyBlockingSocketDisconnectionQueue.getInstance();
        
        EzySocketDataHandlerGroupRemover dataHandlerGroupRemover = mock(EzySocketDataHandlerGroupRemover.class);
        EzySocketDataHandlerGroup handlerGroup = null;
        when(dataHandlerGroupRemover.removeHandlerGroup(any(EzySession.class))).thenReturn(handlerGroup);
        
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketDisconnection disconnection = new EzySimpleSocketDisconnection(session);
        disconnectionQueue.add(disconnection);
        
        handler.setDisconnectionQueue(disconnectionQueue);
        handler.setDataHandlerGroupRemover(dataHandlerGroupRemover);
        handler.handleEvent();
    }
    
    @Test
    public void processDisconnectionQueueExceptionCaseTest() {
        EzySocketDisconnectionHandler handler = new EzySocketDisconnectionHandler();
        
        EzySocketDisconnectionQueue disconnectionQueue = EzyBlockingSocketDisconnectionQueue.getInstance();
        
        EzySocketDataHandlerGroupRemover dataHandlerGroupRemover = mock(EzySocketDataHandlerGroupRemover.class);
        when(dataHandlerGroupRemover.removeHandlerGroup(any(EzySession.class))).thenThrow(new IllegalArgumentException());
        
        EzySession session = spy(EzyAbstractSession.class);
        EzySocketDisconnection disconnection = new EzySimpleSocketDisconnection(session);
        disconnectionQueue.add(disconnection);
        
        handler.setDisconnectionQueue(disconnectionQueue);
        handler.setDataHandlerGroupRemover(dataHandlerGroupRemover);
        handler.handleEvent();
    }
    
    @Test
    public void processDisconnectionQueueInterrupTest() throws Exception {
        EzySocketDisconnectionHandler handler = new EzySocketDisconnectionHandler();
        
        EzySocketDisconnectionQueue disconnectionQueue = EzyBlockingSocketDisconnectionQueue.getInstance();
        
        EzySocketDataHandlerGroupRemover dataHandlerGroupRemover = mock(EzySocketDataHandlerGroupRemover.class);
        EzySocketDataHandlerGroup handlerGroup = mock(EzySocketDataHandlerGroup.class);
        when(dataHandlerGroupRemover.removeHandlerGroup(any(EzySession.class))).thenReturn(handlerGroup);
        
        handler.setDisconnectionQueue(disconnectionQueue);
        handler.setDataHandlerGroupRemover(dataHandlerGroupRemover);
        Thread thread = new Thread(() -> handler.handleEvent());
        thread.start();
        Thread.sleep(100L);
        thread.interrupt();
    }
    
}

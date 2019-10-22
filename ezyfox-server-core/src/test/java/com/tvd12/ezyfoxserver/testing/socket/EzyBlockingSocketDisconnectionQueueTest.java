package com.tvd12.ezyfoxserver.testing.socket;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnection;

public class EzyBlockingSocketDisconnectionQueueTest {

    @Test
    public void test() {
        EzyBlockingSocketDisconnectionQueue queue = EzyBlockingSocketDisconnectionQueue.getInstance();
        assert queue.isEmpty();
        assert queue.size() == 0;
        
        EzySocketDisconnection disconnection = mock(EzySocketDisconnection.class);
        queue.add(disconnection);
        assert queue.size() == 1;
        queue.remove(disconnection);
        assert queue.size() == 0;
    }
    
}

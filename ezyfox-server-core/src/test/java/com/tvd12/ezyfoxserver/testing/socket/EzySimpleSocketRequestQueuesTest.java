package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;

import static org.mockito.Mockito.*;

public class EzySimpleSocketRequestQueuesTest {

    @Test
    public void test() {
        EzySimpleSocketRequestQueues queues = new EzySimpleSocketRequestQueues();
        EzySocketRequest request = mock(EzySocketRequest.class);
        when(request.isSystemRequest()).thenReturn(true);
        queues.add(request);
        request = mock(EzySocketRequest.class);
        when(request.isSystemRequest()).thenReturn(false);
        queues.add(request);
        assert queues.getExtensionQueue().size() == 1;
        assert queues.getSystemQueue().size() == 1;
    }
    
}

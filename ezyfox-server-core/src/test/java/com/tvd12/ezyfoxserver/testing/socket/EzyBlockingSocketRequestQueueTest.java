package com.tvd12.ezyfoxserver.testing.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import static org.mockito.Mockito.*;

public class EzyBlockingSocketRequestQueueTest {

    @Test
    public void test() {
        EzyBlockingSocketRequestQueue queue = new EzyBlockingSocketRequestQueue(2) {
            @Override
            protected BlockingQueue<EzySocketRequest> newQueue(int capacity) {
                return new LinkedBlockingQueue<>(capacity);
            }
        };
        assert queue.isEmpty();
        assert queue.size() == 0;
        EzySocketRequest request = mock(EzySocketRequest.class);
        assert queue.add(request);
        assert !queue.isFull();
        assert queue.add(request);
        assert queue.isFull();
        assert !queue.add(request);
        queue.remove(request);
        assert queue.size() == 1;
        
    }
    
}

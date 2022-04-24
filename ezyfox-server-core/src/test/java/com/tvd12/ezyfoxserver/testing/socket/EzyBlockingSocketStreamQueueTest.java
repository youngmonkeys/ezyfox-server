package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketStream;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzyBlockingSocketStreamQueueTest {

    @Test
    public void test() {
        EzyBlockingSocketStreamQueue queue = new EzyBlockingSocketStreamQueue(2);
        EzySocketStream stream = mock(EzySocketStream.class);
        assert queue.isEmpty();
        assert queue.add(stream);
        assert !queue.isFull();
        assert !queue.isEmpty();
        assert queue.add(stream);
        assert queue.isFull();
        assert !queue.add(stream);
        queue.remove(stream);
        assert queue.size() == 1;
    }
}

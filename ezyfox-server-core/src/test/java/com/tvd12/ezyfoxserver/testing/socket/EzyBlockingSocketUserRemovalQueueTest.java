package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketUserRemovalQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemoval;

import static org.mockito.Mockito.*;

public class EzyBlockingSocketUserRemovalQueueTest {

    @Test
    public void test() {
        EzyBlockingSocketUserRemovalQueue queue = new EzyBlockingSocketUserRemovalQueue();
        queue.clear();
        assert queue.isEmpty();
        assert queue.size() == 0;
        
        EzySocketUserRemoval removal = mock(EzySocketUserRemoval.class);
        queue.add(removal);
        assert queue.size() == 1;
        queue.remove(removal);
        assert queue.size() == 0;
        
        try {
            queue.add(removal);
            System.out.println(queue.take());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}

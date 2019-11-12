package com.tvd12.ezyfoxserver.testing.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.test.base.BaseTest;

public class EzyBlockingSessionTicketsQueueTest extends BaseTest {

    @Test
    public void test() {
        EzyBlockingSessionTicketsQueue queue = new EzyBlockingSessionTicketsQueue();
        List<MySession> sessions = new ArrayList<>();
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        sessions.add(new MySession());
        
        assert queue.isEmpty();
        assert queue.size() == 0;
        
        for(int i = 0 ; i < 10 ; ++i) {
            queue.add(sessions.get(0));
        }
        
        for(int i = 0 ; i < 10 ; ++i) {
            queue.add(sessions.get(i));
        }
        
        assert queue.add(sessions.get(0));
        
    }
    
    public static class MySession extends EzyAbstractSession {
        private static final long serialVersionUID = -6353939578697084098L;
        
        public static final AtomicInteger ID_GENTOR = new AtomicInteger(); 
        
        public MySession() {
            super();
            this.id = ID_GENTOR.incrementAndGet();
        }
        
    }
    
}

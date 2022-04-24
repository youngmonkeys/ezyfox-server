package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

        for (int i = 0; i < 10; ++i) {
            queue.add(sessions.get(0));
        }

        for (int i = 0; i < 10; ++i) {
            queue.add(sessions.get(i));
        }

        assert queue.add(sessions.get(0));

    }

    @Test
    public void takeInactiveSession() throws Exception {
        // given
        EzyBlockingSessionTicketsQueue sut = new EzyBlockingSessionTicketsQueue();

        MySession session = new MySession();
        session.setActivated(false);
        sut.add(session);

        Thread newThread = new Thread(() -> {
            // when
            EzySession takeSession;
            try {
                takeSession = sut.take();
            } catch (InterruptedException e) {
                return;
            }

            // then
            Asserts.assertEquals(session, takeSession);
        });
        newThread.start();
        Thread.sleep(300L);
        newThread.interrupt();
    }

    public static class MySession extends EzyAbstractSession {
        public static final AtomicInteger ID_GENTOR = new AtomicInteger();
        private static final long serialVersionUID = -6353939578697084098L;

        public MySession() {
            super();
            this.id = ID_GENTOR.incrementAndGet();
        }

    }
}

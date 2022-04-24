package com.tvd12.ezyfoxserver.testing.wrapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicLong;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import com.tvd12.test.assertion.Asserts;

public class V121SessionManagerTest {

    @Test
    public void inspectTest() throws Exception {
        // given
        EzySocketDisconnectionQueue disconnectionQueue =
                new EzyBlockingSocketDisconnectionQueue();
        SessionManager sut = (SessionManager) new SessionManager.Builder()
                .validationInterval(100)
                .validationDelay(100)
                .objectFactory(() -> new Session(disconnectionQueue))
                .tokenGenerator(new EzySimpleSessionTokenGenerator())
                .build();
        EzyChannel channel1 = mock(EzyChannel.class);
        when(channel1.getConnection()).thenReturn(new Object());
        EzyChannel channel2 = mock(EzyChannel.class);
        when(channel2.getConnection()).thenReturn(new Object());
        EzyChannel channel3 = mock(EzyChannel.class);
        when(channel3.getConnection()).thenReturn(new Object());

        Session session1 = sut.provideSession(channel1);
        session1.setLoggedIn(true);
        session1.setLastReadTime(System.currentTimeMillis());
        Session session2 = sut.provideSession(channel2);
        session2.setLoggedIn(true);
        session2.setLastReadTime(System.currentTimeMillis());
        Session session3 = sut.provideSession(channel3);
        session3.setLoggedIn(true);
        session3.setLastReadTime(System.currentTimeMillis());

        Thread[] threads = new Thread[10];
        for(int i = 0 ; i < threads.length ; ++i) {
            threads[i] = new Thread(() -> {
                long start = System.currentTimeMillis();
                long elapsedTime = 0;
                while(elapsedTime < 100) {
                    EzyChannel channel = mock(EzyChannel.class);
                    when(channel.getConnection()).thenReturn(new Object());
                    sut.provideSession(channel);
                    elapsedTime = System.currentTimeMillis() - start;
                }
            });
        }

        Thread disconnectionThread = new Thread(() -> {
            while(true) {
                try {
                    EzySocketDisconnection item = disconnectionQueue.take();
                    sut.clearSession((Session) item.getSession());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        disconnectionThread.start();

        // when
        sut.start();

        for(int i = 0 ; i < threads.length ; ++i) {
            threads[i].start();
        }
        Thread.sleep(1000);
        disconnectionThread.interrupt();


        // then
        Asserts.assertEquals(3, sut.getAllSessionCount());
        sut.destroy();
    }

    private static class Session extends EzyAbstractSession {
        private static final long serialVersionUID = -3368343048551431719L;

        private static final AtomicLong ID_GENTOR = new AtomicLong();

        private Session(EzySocketDisconnectionQueue disconnectionQueue) {
            this.maxWaitingTime = 100;
            this.id = ID_GENTOR.incrementAndGet();
            this.activated = true;
            this.disconnectionQueue = disconnectionQueue;
        }
    }

    private static class SessionManager extends EzySimpleSessionManager<Session> {

        private SessionManager(Builder builder) {
            super(builder);
        }

        private static class Builder extends EzySimpleSessionManager.Builder<Session> {

            @Override
            public SessionManager build() {
                return new SessionManager(this);
            }
        }
    }
}

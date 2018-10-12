package com.tvd12.ezyfoxserver.testing.entity;

import static org.testng.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSession;

public class EzyAbstractSessionTest extends BaseCoreTest {

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void test() {
        MyTestSession session = new MyTestSession();
        session.setId(100);
        session.setPrivateKey(new byte[] {1, 2, 3});
        session.addReadBytes(10);
        session.addWrittenBytes(10);
        session.setLoggedInTime(12345);
        session.setMaxWaitingTime(123);
        session.setMaxIdleTime(12345L);
        assertEquals(session.getPrivateKey(), new byte[] {1, 2, 3});
        assertEquals(session.getReadBytes(), 10);
        assertEquals(session.getWrittenBytes(), 10);
        assertEquals(session.getLoggedInTime(), 12345L);
        assertEquals(session.getMaxWaitingTime(), 123L);
        MyTestSession session2 = new MyTestSession();
        session2.setId(1);
        
        MyTestSession session3 = new MyTestSession();
        session3.setId(100);
        
        assert !session.equals(null);
        assert session.equals(session);
        assert !session.equals(new Object());
        assert !session.equals(session2);
        assert !session.equals(new PrivateSession());
        assert session.equals(session3);
        assert session.hashCode() != session2.hashCode();
    }
    
    private static class PrivateSession extends EzyAbstractSession {
        private static final long serialVersionUID = -3656335144134244222L;

        @Override
        public SocketAddress getClientAddress() {
            return new InetSocketAddress(10);
        }

        @Override
        public SocketAddress getServerAddress() {
            return new InetSocketAddress(10);
        }

        @Override
        public void close() {
            
        }

        @Override
        public void disconnect(EzyConstant disconnectReason) {
        }
        
    }
}

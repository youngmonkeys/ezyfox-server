package com.tvd12.ezyfoxserver.testing.wrapper;

import static org.testng.Assert.assertEquals;

import java.util.UUID;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.delegate.EzyAbstractSessionDelegate;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSession;
import com.tvd12.ezyfoxserver.testing.MyTestSessionManager;
import static org.mockito.Mockito.*;

public class EzySimpleSessionManagerTest extends BaseCoreTest {

    @Test
    public void testNormalCase() throws Exception {
        MyTestSessionManager manager = (MyTestSessionManager) new MyTestSessionManager.Builder()
                .validationDelay(5)
                .validationInterval(5)
                .build();
        MyTestSession session = manager.provideSession(EzyConnectionType.SOCKET);
        manager.removeSession(session);
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setDelegate(new EzyAbstractSessionDelegate() {});
        manager.removeSession(session, EzyDisconnectReason.IDLE);
        manager.removeSession(null, EzyDisconnectReason.IDLE);
        
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setDelegate(new EzyAbstractSessionDelegate() {
        });
        session.setLoggedIn(true);
        manager.addLoggedInSession(session);
        assertEquals(manager.getLoggedInSessions(), Lists.newArrayList(session));

        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setActivated(true);
        session.setLoggedIn(false);
        session.setCreationTime(System.currentTimeMillis() - 1000);
        session.setMaxWaitingTime(100);
        
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(false);
        session.setCreationTime(System.currentTimeMillis());
        session.setMaxWaitingTime(10000);
        
        assert manager.getAllSessionCount() == 5;
        assert manager.getAliveSessionCount() == 5;
        assert manager.getLoggedInSessionCount() == 1;
        
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setActivated(true);
        session.setLoggedIn(true);
        session.setLastReadTime(0);
        session.setCreationTime(System.currentTimeMillis());
        session.setMaxWaitingTime(10000);
        
        try {
            manager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(300);
        
        Object connection = new Object();
        EzyChannel channel = mock(EzyChannel.class);
        when(channel.getConnection()).thenReturn(connection);
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        session = manager.provideSession(channel);
        
        assert manager.getSession(session.getId()) == session;
        assert manager.getSession(connection) == session;
        
        manager.clearSession(session);
        
        Thread.sleep(300);
        
        manager.destroy();
    }
    
    @Test
    public void test1() {
        MyTestSessionManager manager = 
                (MyTestSessionManager) new MyTestSessionManager.Builder()
                .tokenGenerator(new EzySessionTokenGenerator() {
                    
                    @Override
                    public String generate() {
                        return UUID.randomUUID().toString();
                    }
                })
                .build();
        MyTestSession session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(true);
        manager.addLoggedInSession(session);
        MyTestSession session2 = manager.provideSession(EzyConnectionType.SOCKET);
        assert session.getId() != session2.getId();
        assertEquals(manager.getAllSessions(), Lists.newArrayList(session, session2));
        
    }
    
}

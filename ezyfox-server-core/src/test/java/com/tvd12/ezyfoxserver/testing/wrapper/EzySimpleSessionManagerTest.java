package com.tvd12.ezyfoxserver.testing.wrapper;

import static org.testng.Assert.assertEquals;

import java.util.UUID;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.delegate.EzyAbstractSessionDelegate;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSession;
import com.tvd12.ezyfoxserver.testing.MyTestSessionManager;

public class EzySimpleSessionManagerTest extends BaseCoreTest {

    @Test
    public void test() {
        MyTestSessionManager manager = new MyTestSessionManager.Builder()
                .build();
        MyTestSession session = manager.provideSession(EzyConnectionType.SOCKET);
        manager.removeSession(session);
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setDelegate(new EzyAbstractSessionDelegate() {
        });
        manager.removeSession(session, EzyDisconnectReason.IDLE);
        manager.removeSession(null, EzyDisconnectReason.IDLE);
        
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setDelegate(new EzyAbstractSessionDelegate() {
        });
        session.setLoggedIn(true);
        manager.addLoggedInSession(session);
        assertEquals(manager.getLoggedInSessions(), Lists.newArrayList(session));

        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(false);
        session.setCreationTime(System.currentTimeMillis() - 1000);
        session.setMaxWaitingTime(100);
        
        session = manager.provideSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(false);
        session.setCreationTime(System.currentTimeMillis());
        session.setMaxWaitingTime(10000);
        
        try {
            manager.start();
        } catch (Exception e) {
        }
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

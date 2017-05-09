package com.tvd12.ezyfoxserver.testing.wrapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.UUID;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
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
        MyTestSession session = manager.borrowSession(EzyConnectionType.SOCKET);
        manager.returnSession(session);
        session = manager.borrowSession(EzyConnectionType.SOCKET);
        session.setDelegate(new EzyAbstractSessionDelegate() {
        });
        manager.returnSession(session, EzySessionRemoveReason.IDLE);
        manager.returnSession(null, EzySessionRemoveReason.IDLE);
        
        session = manager.borrowSession(EzyConnectionType.SOCKET);
        session.setDelegate(new EzyAbstractSessionDelegate() {
        });
        session.setLoggedIn(true);
        manager.addLoggedInSession(session);
        assertEquals(manager.getLoggedInSessions(), Lists.newArrayList(session));

        assertFalse(manager.isStaleObject(session));
        
        session = manager.borrowSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(false);
        session.setCreationTime(System.currentTimeMillis() - 1000);
        session.setMaxWaitingTime(100);
        assertTrue(manager.isStaleObject(session));
        
        manager.removeStaleObject(session);
        
        session = manager.borrowSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(false);
        session.setCreationTime(System.currentTimeMillis());
        session.setMaxWaitingTime(10000);
        assertFalse(manager.isStaleObject(session));
        
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
        MyTestSession session = manager.borrowSession(EzyConnectionType.SOCKET);
        session.setLoggedIn(true);
        manager.addLoggedInSession(session);
        MyTestSession session2 = manager.borrowSession(EzyConnectionType.SOCKET);
        assert session.getId() != session2.getId();
        assertEquals(manager.getBorrowedObjects(), Lists.newArrayList(session, session2));
        assertEquals(manager.getCanBeStaleObjects(), Lists.newArrayList(session2));
        
    }
    
}

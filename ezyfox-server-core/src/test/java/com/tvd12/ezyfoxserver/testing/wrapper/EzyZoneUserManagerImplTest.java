package com.tvd12.ezyfoxserver.testing.wrapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySimpleUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.testing.socket.TestBlockingSocketUserRemovalQueue;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodUtil;

public class EzyZoneUserManagerImplTest {

    @Test
    public void test() throws Exception {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzySimpleUserDelegate userDelegate = new EzySimpleUserDelegate(serverContext, queue);
        userDelegate = new EzySimpleUserDelegate(serverContext, queue);
        
        EzyZoneUserManagerImpl manager = (EzyZoneUserManagerImpl) EzyZoneUserManagerImpl.builder()
                .idleValidationDelay(10)
                .idleValidationInterval(10)
                .idleValidationThreadPoolSize(1)
                .userDelegate(userDelegate)
                .maxIdleTime(10)
                .build();
        EzyAbstractSession session1 = mock(EzyAbstractSession.class);
        EzySimpleUser user1 = new EzySimpleUser();
        user1.setZoneId(1);
        user1.setName("user1");
        user1.setMaxIdleTime(0);
        manager.addUser(session1, user1);
        assert manager.getUserCount() == 1;
        assert manager.getUser(session1) == user1;
        EzyAbstractSession session12 = mock(EzyAbstractSession.class);
        manager.bind(session12, user1);
        manager.unmapSessionUser(session12, EzyUserRemoveReason.EXIT_APP);
        assert manager.getUserCount() == 0;
        
        EzyAbstractSession session2 = mock(EzyAbstractSession.class);
        EzySimpleUser user2 = new EzySimpleUser();
        user2.setZoneId(1);
        user2.setName("user2");
        user2.setMaxIdleTime(Integer.MAX_VALUE);
        user2.addSession(session2);
        manager.addUser(session2, user2);
        assert manager.getUserCount() == 1;
        
        newAndAddIdleUser("user3", manager);
        assert manager.getUserCount() == 2;
        
        newAndAddIdleUser("user4", manager);
        assert manager.getUserCount() == 3;
        
        manager.start();
        
        Thread.sleep(300);
        
        assert manager.getUserCount() == 1;
        
        manager.destroy();
    }
    
    @Test
    public void unmapSessionUserWithUserIsNull() {
    	// given
    	EzyZoneUserManagerImpl sut = newZoneUserManager();
    	EzySession session = mock(EzySession.class);
    	
    	// when
    	sut.unmapSessionUser(session, mock(EzyConstant.class));
    	
    	// then
    	Asserts.assertNull(sut.getUser(session));
    }
    
    @Test
    public void unmapSessionUserWithSessionCountGreaterThan1() {
    	// given
    	EzyZoneUserManagerImpl sut = newZoneUserManager();
    	EzySession session = mock(EzySession.class);

    	EzyUser user = mock(EzyUser.class);
    	when(user.getSessionCount()).thenReturn(1);
    	
    	Map<EzySession, EzyUser> usersBySession = FieldUtil.getFieldValue(sut, "usersBySession");
    	usersBySession.put(session, user);
    	
    	// when
    	sut.unmapSessionUser(session, mock(EzyConstant.class));
    	
    	// then
    	Asserts.assertNull(sut.getUser(session));
    }
    
    @Test
    public void unmapSessionUserWithmaxIdleTimeGreaterThan0() {
    	// given
    	EzyZoneUserManagerImpl sut = newZoneUserManager();
    	EzySession session = mock(EzySession.class);

    	EzyUser user = mock(EzyUser.class);
    	when(user.getMaxIdleTime()).thenReturn(1000L);
    	
    	Map<EzySession, EzyUser> usersBySession = FieldUtil.getFieldValue(sut, "usersBySession");
    	usersBySession.put(session, user);
    	
    	// when
    	sut.unmapSessionUser(session, mock(EzyConstant.class));
    	
    	// then
    	Asserts.assertNull(sut.getUser(session));
    }
    
    @Test
    public void startIdleValidationServiceWithIdleValidationService() {
    	// given
    	EzyZoneUserManagerImpl sut = newZoneUserManager();

    	// when
    	MethodUtil.invokeMethod("startIdleValidationService", sut);
    	
    	// then
    	Asserts.assertNull(FieldUtil.getFieldValue(sut, "idleValidationService"));
    }
    
    @Test
    public void destroyWithIdleValidationService() {
    	// given
    	EzyZoneUserManagerImpl sut = newZoneUserManager();

    	// when
    	sut.destroy();
    	
    	// then
    	Asserts.assertNull(FieldUtil.getFieldValue(sut, "idleValidationService"));
    }
    
    private EzySimpleUser newAndAddIdleUser(String username, EzyZoneUserManagerImpl manager) {
        EzyAbstractSession session = mock(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setZoneId(1);
        user.setName("user3");
        user.setMaxIdleTime(Integer.MAX_VALUE);
        manager.addUser(session, user);
        user.removeSession(session);
        user.setMaxIdleTime(0);
        return user;
    }
    
    private EzyZoneUserManagerImpl newZoneUserManager() {
    	EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzySimpleUserDelegate userDelegate = new EzySimpleUserDelegate(serverContext, queue);
        userDelegate = new EzySimpleUserDelegate(serverContext, queue);
        
        return (EzyZoneUserManagerImpl) EzyZoneUserManagerImpl.builder()
                .idleValidationDelay(10)
                .idleValidationInterval(10)
                .idleValidationThreadPoolSize(1)
                .userDelegate(userDelegate)
                .build();
    }
}

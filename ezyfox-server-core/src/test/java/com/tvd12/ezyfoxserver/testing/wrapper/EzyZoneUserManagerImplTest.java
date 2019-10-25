package com.tvd12.ezyfoxserver.testing.wrapper;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySimpleUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.testing.socket.TestBlockingSocketUserRemovalQueue;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import static org.mockito.Mockito.*;

public class EzyZoneUserManagerImplTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzySimpleUserDelegate userDelegate = new EzySimpleUserDelegate(serverContext);
        userDelegate = new EzySimpleUserDelegate(serverContext, queue);
        
        EzyZoneUserManagerImpl manager = (EzyZoneUserManagerImpl) EzyZoneUserManagerImpl.builder()
                .idleValidationDelay(10)
                .idleValidationInterval(10)
                .idleValidationThreadPoolSize(1)
                .userDelegate(userDelegate)
                .build();
        EzyAbstractSession session1 = mock(EzyAbstractSession.class);
        EzySimpleUser user1 = new EzySimpleUser();
        user1.setZoneId(1);
        user1.setName("user1");
        user1.setMaxIdleTime(0);
        manager.bind(session1, user1);
        assert manager.getUser(session1) == user1;
        manager.unmapSessionUser(session1, EzyUserRemoveReason.EXIT_APP);
        
    }
    
}

package com.tvd12.ezyfoxserver.testing.entity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.concurrent.locks.Lock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.test.base.BaseTest;

public class EzySimpleUserTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleUser user = new EzySimpleUser();
        user.setId(1);
        user.setPassword("abc");
        assert user.getPassword().equals("abc");
        user.setName("dungtv1");
        
        EzySimpleUser user2 = new EzySimpleUser();
        user2.setPassword("abc");
        user2.setId(2);
        assert user2.getPassword().equals("abc");
        user.setName("dungtv2");
        
        assert !user.equals(user2);
        assert user.hashCode() != user2.hashCode();
        
        assert user.getLocks() != null;
        assert user.getSessionMap() != null;
        assert user.getMaxSessions() == 30;
        assert user.getStartIdleTime() > 0;
        assert !user.isDestroyed();
        
        assert user.getSession() == null;
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyImmediateDeliver immediateDeliver = mock(EzyImmediateDeliver.class);
        EzySocketDisconnectionQueue disconnectionQueue = mock(EzySocketDisconnectionQueue.class);
        session.setImmediateDeliver(immediateDeliver);
        session.setDisconnectionQueue(disconnectionQueue);
        user.addSession(session);
        assert user.getSession() == session;
        
        Lock lock = user.getLock("test");
        assert lock != null;
        
        EzyPacket packet = mock(EzyPacket.class);
        user.send(packet);
        user.sendNow(packet);
        
        user.disconnect(EzyDisconnectReason.IDLE);
        user.disconnect();
        
        user.removeSession(session);
        
        user.setMaxIdleTime(100 * 60 * 1000);
        assert !user.isIdle();
        user.setMaxIdleTime(-1);
        assert user.isIdle();
        
        user.destroy();
        user.destroy();
        assert user.getSessions().size() == 0;
    }
    
}

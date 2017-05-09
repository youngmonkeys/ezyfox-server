package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.command.impl.EzyDisconnectUserImpl;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;

public class EzyDisconnectUserImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySession session = newSession();
        session.setReconnectToken("abc");
        EzySimpleUser user = newUser();
        user.setName("abc");
        user.addSession(session);
        EzyServerContext context = newServerContext();
        new EzyDisconnectUserImpl(context)
            .user(user)
            .fireClientEvent(true)
            .fireServerEvent(true)
            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
            .execute();
    }
    
    @Test
    public void test1() {
        EzySession session = newSession();
        EzySimpleUser user = newUser();
        session.setReconnectToken("abc");
        user.setName("abc");
        user.addSession(session);
        EzyServerContext context = newServerContext();
        new EzyDisconnectUserImpl(context)
            .user(user)
            .fireClientEvent(false)
            .fireServerEvent(true)
            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
            .execute();
    }
    
    @Test
    public void test2() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzySession session = newSession();
        session.setReconnectToken("abc");
        user.addSession(session);
        EzyServerContext context = newServerContext();
        new EzyDisconnectUserImpl(context)
            .user(user)
            .fireClientEvent(false)
            .fireServerEvent(false)
            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
            .execute();
    }
    
    @Test
    public void test3() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzySession session = newSession();
        session.setReconnectToken("abc");
        user.addSession(session);
        EzyServerContext context = newServerContext();
        new EzyDisconnectUserImpl(context)
            .user(user)
            .fireClientEvent(false)
            .fireServerEvent(true)
            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
            .execute();
    }
    
}

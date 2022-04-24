package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;
import org.testng.annotations.Test;

public class EzyDisconnectUserImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySession session = newSession();
        session.setToken("abc");
        EzySimpleUser user = newUser();
        user.setName("abc");
        user.addSession(session);
//        EzyServerContext context = newServerContext();
//        new EzyDisconnectUserImpl(context)
//            .user(user)
//            .fireClientEvent(true)
//            .fireServerEvent(true)
//            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
//            .execute();
    }

    @Test
    public void test1() {
        EzySession session = newSession();
        EzySimpleUser user = newUser();
        session.setToken("abc");
        user.setName("abc");
        user.addSession(session);
//        EzyServerContext context = newServerContext();
//        new EzyDisconnectUserImpl(context)
//            .user(user)
//            .fireClientEvent(false)
//            .fireServerEvent(true)
//            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
//            .execute();
    }

    @Test
    public void test2() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzySession session = newSession();
        session.setToken("abc");
        user.addSession(session);
//        EzyServerContext context = newServerContext();
//        new EzyDisconnectUserImpl(context)
//            .user(user)
//            .fireClientEvent(false)
//            .fireServerEvent(false)
//            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
//            .execute();
    }

    @Test
    public void test3() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzySession session = newSession();
        session.setToken("abc");
        user.addSession(session);
//        EzyServerContext context = newServerContext();
//        new EzyDisconnectUserImpl(context)
//            .user(user)
//            .fireClientEvent(false)
//            .fireServerEvent(true)
//            .reason(EzyDisconnectReason.ANOTHER_SESSION_LOGIN)
//            .execute();
    }
}

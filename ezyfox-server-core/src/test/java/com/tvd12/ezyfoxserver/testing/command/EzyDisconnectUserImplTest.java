package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketDisconnection;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

public class EzyDisconnectUserImplTest extends BaseCoreTest {

    @Test
    public void test() {
        // given
        String token = RandomUtil.randomShortAlphabetString();
        EzySession session = newSession();
        session.setToken(token);

        String username = RandomUtil.randomShortAlphabetString();
        EzySimpleUser user = newUser();
        user.setName(username);
        user.addSession(session);

        // when
        EzySimpleSocketDisconnection disconnection = new EzySimpleSocketDisconnection(
            session,
            EzyDisconnectReason.ADMIN_BAN
        );

        // when
        Asserts.assertEquals(user.getName(), username);
        Asserts.assertEquals(user.getSession(), session);
        Asserts.assertEquals(session.getToken(), token);
        Asserts.assertEquals(disconnection.getSession(), session);
        Asserts.assertEquals(disconnection.getDisconnectReason(), EzyDisconnectReason.ADMIN_BAN);
    }

    @Test
    public void test1() {
        EzySession session = newSession();
        EzySimpleUser user = newUser();
        session.setToken("abc");
        user.setName("abc");
        user.addSession(session);
    }

    @Test
    public void test2() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzySession session = newSession();
        session.setToken("abc");
        user.addSession(session);
    }

    @Test
    public void test3() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzySession session = newSession();
        session.setToken("abc");
        user.addSession(session);
    }
}

package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSession;
import com.tvd12.ezyfoxserver.testing.MyTestUser;
import com.tvd12.ezyfoxserver.testing.MyTestUserManager;
import org.testng.annotations.Test;

public class EzyUserManagerImplTest extends BaseCoreTest {

    @Test
    public void test() {
        MyTestUserManager manager = MyTestUserManager.builder()
            .build();
        MyTestSession session = new MyTestSession();
        session.setId(10);
        MyTestUser user = new MyTestUser();
        user.setId(1);
        user.setName("dungtv");
        session.setToken("123456");
        user.addSession(session);
        manager.addUser(session, user);

        assert manager.getUser(1) == user;
        assert manager.getUser("dungtv") == user;
        assert manager.containsUser(1);
        assert !manager.containsUser(-1);
        assert manager.containsUser(session);
        assert manager.containsUser(user);
        manager.removeUser(user.getId());
        assert manager.getUser(1) == null;

        manager.addUser(session, user);
        manager.removeUser(user.getName());
        assert manager.getUser("dungtv") == null;
        manager.addUser(session, user);
        manager.removeUser((EzyUser) null);
    }

}

package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleUserManager;
import org.testng.annotations.Test;

public class EzySimpleUserManagerTest {

    @Test
    public void test() {
        EzySimpleUserManager manager = (EzySimpleUserManager) EzySimpleUserManager.builder()
            .maxUsers(3)
            .build();
        manager = new EzySimpleUserManager(1);
        assert manager.getMaxUsers() == 1;
        assert manager.available();
        assert manager != null;

        EzySimpleUser user1 = new EzySimpleUser();
        user1.setName("user1");
        manager.addUser(user1);

        try {
            EzySimpleUser user2 = new EzySimpleUser();
            user2.setName("user2");
            manager.addUser(user2);
        } catch (Exception e) {
            assert e instanceof EzyMaxUserException;
        }

        assert !manager.available();
        manager.getLock(user1.getName());

        manager.clear();
        manager.destroy();

    }

}

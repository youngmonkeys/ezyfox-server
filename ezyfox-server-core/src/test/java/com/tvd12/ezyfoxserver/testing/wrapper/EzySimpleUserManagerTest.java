package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleUserManager;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

public class EzySimpleUserManagerTest {

    @Test
    public void fromBuilderTest() {
        int maxUsers = RandomUtil.randomInt();
        EzySimpleUserManager manager = (EzySimpleUserManager) EzySimpleUserManager.builder()
            .maxUsers(maxUsers)
            .build();
        assert manager.getMaxUsers() == maxUsers;
        assert manager.available();

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

        assert manager.available();
        manager.getLock(user1.getName());

        manager.clear();
        manager.destroy();
    }

    @Test
    public void fromConstructorTest() {
        EzySimpleUserManager manager = new EzySimpleUserManager(1);
        assert manager.getMaxUsers() == 1;
        assert manager.available();

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

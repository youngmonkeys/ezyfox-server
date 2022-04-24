package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.wrapper.EzySynchronizedUserManager;
import org.testng.annotations.Test;

public class EzySynchronizedUserManagerTest {

    @Test
    public void test() {
        new EzySynchronizedUserManager(100);
        EzySynchronizedUserManager userManager = EzySynchronizedUserManager.builder()
            .maxUsers(2)
            .build();
        EzySimpleUser user1 = new EzySimpleUser();
        user1.setName("user1");
        EzySimpleUser user2 = new EzySimpleUser();
        user2.setName("user2");

        assert userManager.addUser(user1) == null;
        assert userManager.addUser(user1) != null;
        assert userManager.addUser(user2) == null;
        assert userManager.getUser(user1.getId()) == user1;
        assert userManager.getUser(user1.getName()) == user1;
        assert userManager.getUserList().size() == 2;
        assert userManager.containsUser(user2.getId());
        assert userManager.containsUser(user2.getName());
        assert userManager.removeUser(user2) == user2;
        assert userManager.getUserCount() == 1;
        assert userManager.available();
        assert userManager.getLock(user1.getName()) != null;
        assert userManager.getSynchronizedLock() != null;
        userManager.removeLock(user1.getName());
        userManager.clear();
        userManager.destroy();

    }
}

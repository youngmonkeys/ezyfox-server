package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.wrapper.EzyDefaultUserManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

public class EzyDefaultUserManagerTest {

    @Test
    public void formBuilderTest() {
        // given
        int maxUsers = RandomUtil.randomInt();

        // when
        EzyDefaultUserManager manager = (EzyDefaultUserManager) EzyDefaultUserManager.builder()
            .maxUsers(maxUsers)
            .build();

        // then
        Asserts.assertEquals(manager.getMaxUsers(), maxUsers);
    }

    @Test
    public void fromConstructorTest() {
        // given
        int maxUsers = RandomUtil.randomInt();

        // when
        EzyDefaultUserManager manager = new EzyDefaultUserManager(
            maxUsers
        );

        // then
        Asserts.assertEquals(manager.getMaxUsers(), maxUsers);
    }
}

package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.wrapper.EzyDefaultUserManager;
import org.testng.annotations.Test;

public class EzyDefaultUserManagerTest {

    @Test
    public void test() {
        EzyDefaultUserManager manager = (EzyDefaultUserManager) EzyDefaultUserManager.builder()
            .maxUsers(3)
            .build();
        manager = new EzyDefaultUserManager(3);
        assert manager != null;
    }

}

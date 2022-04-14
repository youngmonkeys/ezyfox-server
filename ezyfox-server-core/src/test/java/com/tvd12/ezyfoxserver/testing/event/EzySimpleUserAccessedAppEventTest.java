package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserAccessedAppEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzySimpleUserAccessedAppEventTest extends BaseCoreTest {
    @Test
    public void test() {
        EzyUser user = newUser();
        EzySimpleUserAccessedAppEvent event = new EzySimpleUserAccessedAppEvent(user);
        assert event.getUser() == user;
    }
}

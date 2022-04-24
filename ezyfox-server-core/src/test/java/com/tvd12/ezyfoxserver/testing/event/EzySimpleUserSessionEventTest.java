package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.event.EzySimpleUserSessionEvent;
import org.testng.annotations.Test;

public class EzySimpleUserSessionEventTest {

    @Test
    public void test() {
        EzySimpleUserSessionEvent event = new EzySimpleUserSessionEvent(null, null);
        assert event.getSession() == null;
    }

}

package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.event.EzySimpleUserSessionEvent;

public class EzySimpleUserSessionEventTest {

    @Test
    public void test() {
        EzySimpleUserSessionEvent event = new EzySimpleUserSessionEvent(null, null);
        assert event.getSession() == null;
    }
    
}

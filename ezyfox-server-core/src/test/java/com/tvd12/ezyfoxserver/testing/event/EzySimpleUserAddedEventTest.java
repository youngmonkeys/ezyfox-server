package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.event.EzySimpleUserAddedEvent;

public class EzySimpleUserAddedEventTest {

    @Test
    public void test() {
        EzySimpleUserAddedEvent event = new EzySimpleUserAddedEvent(null, null, null);
        assert event.getLoginData() == null;
    }
    
}

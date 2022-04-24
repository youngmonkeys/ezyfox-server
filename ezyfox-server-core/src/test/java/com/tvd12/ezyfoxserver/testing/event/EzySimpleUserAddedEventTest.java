package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.event.EzySimpleUserAddedEvent;
import org.testng.annotations.Test;

public class EzySimpleUserAddedEventTest {

    @Test
    public void test() {
        EzySimpleUserAddedEvent event = new EzySimpleUserAddedEvent(null, null, null);
        assert event.getLoginData() == null;
    }

}

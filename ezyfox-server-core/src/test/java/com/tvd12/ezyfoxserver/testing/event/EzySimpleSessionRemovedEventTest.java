package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.event.EzySimpleSessionRemovedEvent;

public class EzySimpleSessionRemovedEventTest {

    @Test
    public void test() {
        EzySimpleSessionRemovedEvent event = new EzySimpleSessionRemovedEvent(
                null, null, EzyDisconnectReason.ADMIN_BAN);
        assert event.getReason() == EzyDisconnectReason.ADMIN_BAN;
    }
    
}

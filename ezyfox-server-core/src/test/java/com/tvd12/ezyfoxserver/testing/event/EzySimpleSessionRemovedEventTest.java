package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.event.EzySimpleSessionRemovedEvent;
import org.testng.annotations.Test;

public class EzySimpleSessionRemovedEventTest {

    @Test
    public void test() {
        EzySimpleSessionRemovedEvent event = new EzySimpleSessionRemovedEvent(
            null, null, EzyDisconnectReason.ADMIN_BAN);
        assert event.getReason() == EzyDisconnectReason.ADMIN_BAN;
    }
}

package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyDisconnectEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = newUser();
        EzyUserRemovedEvent event = new EzySimpleUserRemovedEvent(
            user, EzyDisconnectReason.IDLE);
        assert event.getUser() == user;
        assert event.getReason() == EzyDisconnectReason.IDLE;
    }
}

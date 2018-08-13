package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyDisconnectEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = newUser();
        EzyUserRemovedEvent event = (EzyUserRemovedEvent) EzySimpleUserRemovedEvent.builder()
                .user(user)
                .reason(EzyDisconnectReason.IDLE)
                .build();
        assert event.getUser() == user;
        assert event.getReason() == EzyDisconnectReason.IDLE;
    }
    
}

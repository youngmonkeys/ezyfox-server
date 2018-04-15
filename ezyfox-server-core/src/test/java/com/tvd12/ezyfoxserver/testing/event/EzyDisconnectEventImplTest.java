package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserDisconnectEvent;
import com.tvd12.ezyfoxserver.event.EzyUserDisconnectEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyDisconnectEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = newUser();
        EzyUserDisconnectEvent event = (EzyUserDisconnectEvent) EzySimpleUserDisconnectEvent.builder()
                .user(user)
                .reason(EzyDisconnectReason.IDLE)
                .build();
        assert event.getUser() == user;
        assert event.getReason() == EzyDisconnectReason.IDLE;
    }
    
}

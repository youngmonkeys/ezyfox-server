package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserReconnectEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserReconnectEventImpl;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyReconnectEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = new EzySimpleUser();
        EzyUserReconnectEvent event = (EzyUserReconnectEvent) EzyUserReconnectEventImpl.builder()
                .user(user)
                .build();
        assert event.getUser() == user;
    }
    
}

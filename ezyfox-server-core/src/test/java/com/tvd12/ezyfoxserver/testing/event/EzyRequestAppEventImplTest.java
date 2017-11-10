package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserRequestAppEventImpl;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyRequestAppEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = newUser();
        EzyArray data = newArrayBuilder().append(100).build();
        EzyUserRequestAppEvent event = (EzyUserRequestAppEvent) EzyUserRequestAppEventImpl.builder()
                .user(user)
                .data(data)
                .build();
        assert event.getData() == data;
        assert event.getUser() == user;
    }
    
}

package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyRequestAppEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = newUser();
        EzyArray data = newArrayBuilder().append(100).build();
        EzyUserRequestAppEvent event = new EzySimpleUserRequestAppEvent(user, null, data);
        assert event.getData() == data;
        assert event.getUser() == user;
    }
}

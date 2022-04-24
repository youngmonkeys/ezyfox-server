package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserAccessedAppEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.test.assertion.Asserts;

import org.testng.annotations.Test;

public class EzySimpleUserAccessedAppEventTest extends BaseCoreTest {
    
    @Test
    public void test() {
        // given
        EzyUser user = newUser();
        EzySimpleUserAccessedAppEvent underTest = new EzySimpleUserAccessedAppEvent(user);
        
        // when
        EzyUser actual = underTest.getUser();
        
        // then
        Asserts.assertEquals(actual, user);
    }
}

package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyEventControllerTest {

    @Test
    public void getEventTypeDefaultTest() {
        // given
        EzyEventControllerEx instance = new EzyEventControllerEx();

        // when
        EzyConstant eventType = instance.getEventType();

        // then
        Asserts.assertNull(eventType);
    }

    public static class EzyEventControllerEx
        implements EzyEventController<Object, Object> {

        @Override
        public void handle(Object ctx, Object event) {}
    }
}

package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractServerEventController;
import org.testng.annotations.Test;

public class EzyAbstractServerEventControllerTest {

    @Test
    public void newTest() {
        new EzyAbstractServerEventController<Object>() {
            @Override
            public void handle(EzyServerContext ctx, Object event) {
            }

        };
    }
}

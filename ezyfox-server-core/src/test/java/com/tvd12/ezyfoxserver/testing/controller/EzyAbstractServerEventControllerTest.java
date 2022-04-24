package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractServerEventController;

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

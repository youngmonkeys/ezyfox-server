package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractZoneEventController;
import org.testng.annotations.Test;

public class EzyAbstractZoneEventControllerTest {

    @Test
    public void newTest() {
        new EzyAbstractZoneEventController<Object>() {

            @Override
            public void handle(EzyZoneContext ctx, Object event) {}
        };
    }
}

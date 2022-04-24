package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;

public class EzyAbstractPluginEventControllerTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void newTest() {
        new EzyAbstractPluginEventController() {

            @Override
            public void handle(Object ctx, Object event) {
            }
        };
    }
}

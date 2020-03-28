package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;

public class EzyPluginRequestControllerTest {

    @Test
    public void test() {
        EzyPluginRequestController.DEFAULT.handle(null, null);
    }
    
}

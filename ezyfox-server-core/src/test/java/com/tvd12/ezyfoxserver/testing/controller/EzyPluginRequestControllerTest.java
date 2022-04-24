package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import org.testng.annotations.Test;

public class EzyPluginRequestControllerTest {

    @Test
    public void test() {
        EzyPluginRequestController.DEFAULT.handle(null, null);
    }
}

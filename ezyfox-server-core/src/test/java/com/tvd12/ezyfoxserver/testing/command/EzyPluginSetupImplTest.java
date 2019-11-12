package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginSetupImpl;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.test.base.BaseTest;

public class EzyPluginSetupImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzyPluginSetupImpl cmd = new EzyPluginSetupImpl(plugin);
        cmd.setRequestController(new EzyPluginRequestController() {
            @Override
            public void handle(EzyPluginContext ctx, EzyUserRequestPluginEvent event) {
            }
        });
    }
    
}

package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.command.impl.EzyAppSetupImpl;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyAppSetupImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleApplication app = new EzySimpleApplication();
        EzyAppSetupImpl cmd = new EzyAppSetupImpl(app);
        cmd.setRequestController(new EzyAppRequestController() {
            @Override
            public void handle(EzyAppContext ctx, EzyUserRequestAppEvent event) {
            }
        });
    }

}

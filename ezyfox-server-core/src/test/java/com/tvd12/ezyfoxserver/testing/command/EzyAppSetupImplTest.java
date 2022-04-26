package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.command.impl.EzyAppSetupImpl;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyAppSetupImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleApplication app = new EzySimpleApplication();
        EzyAppSetupImpl cmd = new EzyAppSetupImpl(app);
        cmd.setRequestController((ctx, event) -> {});
    }
}

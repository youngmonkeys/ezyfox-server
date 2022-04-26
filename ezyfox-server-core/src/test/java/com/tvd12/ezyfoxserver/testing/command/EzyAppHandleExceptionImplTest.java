package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.command.impl.EzyAppHandleExceptionImpl;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyAppHandleExceptionImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        setting.setName("test");
        app.setSetting(setting);
        EzyAppHandleExceptionImpl cmd = new EzyAppHandleExceptionImpl(app);
        cmd.handle(Thread.currentThread(), new IllegalArgumentException("one"));
        app.getExceptionHandlers().addExceptionHandler((thread, throwable) -> {
            throw new IllegalStateException();
        });
        cmd.handle(Thread.currentThread(), new IllegalArgumentException("two"));
    }
}

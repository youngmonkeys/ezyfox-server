package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginHandleExceptionImpl;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyPluginHandleExceptionImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        setting.setName("test");
        plugin.setSetting(setting);
        EzyPluginHandleExceptionImpl cmd = new EzyPluginHandleExceptionImpl(plugin);
        cmd.handle(Thread.currentThread(), new IllegalArgumentException("one"));
        plugin.getExceptionHandlers().addExceptionHandler(new EzyExceptionHandler() {

            @Override
            public void handleException(Thread thread, Throwable throwable) {
                throw new IllegalStateException();
            }
        });
        cmd.handle(Thread.currentThread(), new IllegalArgumentException("two"));
    }

}

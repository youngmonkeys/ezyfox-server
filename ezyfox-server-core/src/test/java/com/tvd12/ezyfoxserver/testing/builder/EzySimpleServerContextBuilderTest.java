package com.tvd12.ezyfoxserver.testing.builder;

import com.tvd12.ezyfoxserver.builder.EzySimpleServerContextBuilder;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

public class EzySimpleServerContextBuilderTest extends BaseTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() {
        EzySimpleServerContextBuilder instance = new EzySimpleServerContextBuilder();
        MethodInvoker.create()
            .object(instance)
            .method("newAppExecutorService")
            .param(EzyAppSetting.class, new EzySimpleAppSetting())
            .invoke();

        MethodInvoker.create()
            .object(instance)
            .method("newPluginExecutorService")
            .param(EzyPluginSetting.class, new EzySimplePluginSetting())
            .invoke();
    }
}

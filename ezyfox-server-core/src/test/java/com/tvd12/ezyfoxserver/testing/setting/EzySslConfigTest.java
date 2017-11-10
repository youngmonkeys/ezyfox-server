package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleSslConfigSetting;
import com.tvd12.test.base.BaseTest;

public class EzySslConfigTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSslConfigSetting config = new EzySimpleSslConfigSetting();
        config.setFile("file");
        config.setLoader("loader");
        config.setContextFactoryBuilder("builder");
    }
    
}

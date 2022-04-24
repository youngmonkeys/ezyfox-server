package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfoxserver.setting.EzySimpleSslConfigSetting;
import com.tvd12.ezyfoxserver.ssl.EzySslContextInitializer;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;

public class EzySslContextInitializerTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSslConfigSetting setting = new EzySimpleSslConfigSetting();
        EzySslContextInitializer initializer = EzySslContextInitializer.builder()
            .homeFolderPath("test-data")
            .sslConfig(setting)
            .build();
        SSLContext sslContext = initializer.init();
        assert sslContext != null;
    }

}

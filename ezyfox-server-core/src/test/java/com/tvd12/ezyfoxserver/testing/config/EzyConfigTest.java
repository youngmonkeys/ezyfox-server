package com.tvd12.ezyfoxserver.testing.config;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyConfigTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleConfig config = new EzySimpleConfig();
        config.setEzyfoxHome("home");
        config.setLoggerConfigFile("abc");
        assert config.toString() != null;
    }
    
}

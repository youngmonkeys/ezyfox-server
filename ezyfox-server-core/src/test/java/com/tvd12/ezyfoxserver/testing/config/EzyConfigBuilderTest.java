package com.tvd12.ezyfoxserver.testing.config;

import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class EzyConfigBuilderTest {

    @Test
    public void test() {
        EzyConfig config = new EzyConfigBuilder()
            .ezyfoxHome(".")
            .printSettings(true)
            .printBanner(true)
            .bannerFile("banner.txt")
            .loggerConfigFile("log.properties")
            .enableAppClassLoader(true)
            .build();
        assertEquals(config.getEzyfoxHome(), ".");
        assertEquals(config.isPrintSettings(), true);
        assertEquals(config.isPrintBanner(), true);
        assertEquals(config.getBannerFile(), "banner.txt");
        assertEquals(config.getLoggerConfigFile(), "log.properties");
        assertEquals(config.isEnableAppClassLoader(), true);
    }

}

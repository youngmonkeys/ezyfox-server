package com.tvd12.ezyfoxserver.testing.config;

import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
        assertTrue(config.isPrintSettings());
        assertTrue(config.isPrintBanner());
        assertEquals(config.getBannerFile(), "banner.txt");
        assertEquals(config.getLoggerConfigFile(), "log.properties");
        assertTrue(config.isEnableAppClassLoader());
    }
}

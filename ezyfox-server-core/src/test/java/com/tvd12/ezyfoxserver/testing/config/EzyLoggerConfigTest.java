package com.tvd12.ezyfoxserver.testing.config;

import java.io.IOException;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.config.EzyLoggerConfig;

public class EzyLoggerConfigTest {

    @Test
    public void configWithNonExistsFile() throws IOException {
        EzyLoggerConfig.getInstance().config("test-data1/settings/config.properties");
    }
}

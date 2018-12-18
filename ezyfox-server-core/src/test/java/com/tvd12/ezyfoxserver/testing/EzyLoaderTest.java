package com.tvd12.ezyfoxserver.testing;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.config.EzyConfig;

public class EzyLoaderTest extends BaseCoreTest {
    
    @Test
    public void test() throws Exception {
        EzySimpleServer server = newServer();
        
        EzyConfig config = server.getConfig();
        assertEquals(config.getEzyfoxHome(), "test-data");
        assertEquals(config.getLoggerConfigFile(), "logback.groovy");
        
    }

    
}

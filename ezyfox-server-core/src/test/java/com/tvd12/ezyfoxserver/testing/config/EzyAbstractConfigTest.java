package com.tvd12.ezyfoxserver.testing.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzyAbstractSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyAbstractConfigTest extends BaseCoreTest {

    @Test
    public void test() {
        Config config = new Config();
        config.setName("config");
        config.setEntryLoader("loader");
        config.setThreadPoolSize(100);
    }
    
    
    public static class Config extends EzyAbstractSetting {
        @Override
        protected AtomicInteger getIdCounter() {
            return new AtomicInteger(0);
        }
    }
}

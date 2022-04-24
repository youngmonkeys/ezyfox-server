package com.tvd12.ezyfoxserver.testing.config;

import com.tvd12.ezyfoxserver.setting.EzyAbstractSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

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

        @Override
        protected String getParentFolder() {
            return "";
        }
    }
}

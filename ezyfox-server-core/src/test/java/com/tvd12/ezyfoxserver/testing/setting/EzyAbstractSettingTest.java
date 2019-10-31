package com.tvd12.ezyfoxserver.testing.setting;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzyAbstractSetting;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractSettingTest extends BaseTest {

    @Test
    public void test() {
        EzyAbstractSetting setting = new Setting();
        setting.setName("name");
        setting.setFolder("folder");
        setting.setZoneId(1);
        setting.setEntryLoader("entry loader");
        setting.setThreadPoolSize(1);
        setting.setConfigFile("config file");
        setting.setHomePath("home");
        assert !setting.equals(new Setting());
    }
    
    public static class Setting extends EzyAbstractSetting {

        protected static final AtomicInteger GENTER = new AtomicInteger();
        
        @Override
        protected String getParentFolder() {
            return "";
        }

        @Override
        protected AtomicInteger getIdCounter() {
            return GENTER;
        }
        
    }
    
}

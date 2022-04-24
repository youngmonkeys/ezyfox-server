package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzyAbstractSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class EzyAbstractSettingTest extends BaseTest {

    @Test
    public void test() {
        // given
        EzyAbstractSetting setting = new Setting();
        setting.setName("name");
        setting.setFolder("folder");
        setting.setZoneId(1);
        setting.setEntryLoader("entry loader");
        setting.setThreadPoolSize(1);
        setting.setConfigFile("config file");
        setting.setHomePath("home");
        setting.setPackageName("x.y.z");
        setting.setActiveProfiles("hello,world");

        // when
        // then
        Asserts.assertNotEquals(new Setting(), setting);
        Assert.assertEquals("x.y.z", setting.getPackageName());
        Asserts.assertEquals("hello,world", setting.getActiveProfiles());
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

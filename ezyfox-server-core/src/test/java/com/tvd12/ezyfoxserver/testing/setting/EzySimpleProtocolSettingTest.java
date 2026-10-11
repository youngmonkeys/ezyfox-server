package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolPropertySetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EzySimpleProtocolSettingTest extends BaseTest {

    @Test
    public void defaultValuesTest() {
        EzySimpleProtocolSetting setting = new EzySimpleProtocolSetting();

        assert setting.isActive();
        assert setting.getAddress().equals("0.0.0.0");
        assert setting.getMaxRequestSize() == 4096;
        assert setting.getPropertyList().isEmpty();
        assert setting.getProperties().isEmpty();
    }

    @Test
    public void getPropertiesCombinesPropertyListAndExplicitPropertiesTest() {
        EzySimpleProtocolPropertySetting property = new EzySimpleProtocolPropertySetting();
        property.setName("from-list");
        property.setValue("list-value");

        EzySimpleProtocolPropertySetting overriddenProperty = new EzySimpleProtocolPropertySetting();
        overriddenProperty.setName("overridden");
        overriddenProperty.setValue("list-value");

        EzySimpleProtocolSetting setting = new EzySimpleProtocolSetting();
        setting.setPropertyList(Arrays.asList(property, overriddenProperty));
        setting.setProperty("from-setting", "setting-value");
        setting.setProperty("overridden", "setting-value");

        Map<String, String> expected = new HashMap<>();
        expected.put("from-list", "list-value");
        expected.put("from-setting", "setting-value");
        expected.put("overridden", "setting-value");

        assert setting.getProperties().equals(expected);
    }

    @Test
    public void toMapTest() {
        EzySimpleProtocolSetting setting = new EzySimpleProtocolSetting();
        setting.setName("tcp");
        setting.setActive(false);
        setting.setAddress("127.0.0.1");
        setting.setPort(1234);
        setting.setSslActive(true);
        setting.setMaxRequestSize(8192);
        setting.setServerCreator("serverCreator");
        setting.setConfigFile("protocol.xml");
        setting.setProperty("key", "value");

        Map<Object, Object> expected = new HashMap<>();
        expected.put("name", "tcp");
        expected.put("active", false);
        expected.put("address", "127.0.0.1");
        expected.put("port", 1234);
        expected.put("sslActive", true);
        expected.put("maxRequestSize", 8192);
        expected.put("serverCreator", "serverCreator");
        expected.put("configFile", "protocol.xml");
        expected.put("properties", setting.getProperties());

        assert setting.toMap().equals(expected);
    }
}

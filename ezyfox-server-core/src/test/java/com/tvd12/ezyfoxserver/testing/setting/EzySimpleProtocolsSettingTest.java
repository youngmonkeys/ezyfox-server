package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzyProtocolSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolsSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.List;

public class EzySimpleProtocolsSettingTest extends BaseTest {

    @Test
    public void setItemAddsProtocolAndGetActiveProtocolsFiltersInactiveItemsTest() {
        EzySimpleProtocolSetting activeProtocol = new EzySimpleProtocolSetting();
        activeProtocol.setName("active");

        EzySimpleProtocolSetting inactiveProtocol = new EzySimpleProtocolSetting();
        inactiveProtocol.setName("inactive");
        inactiveProtocol.setActive(false);

        EzySimpleProtocolsSetting setting = new EzySimpleProtocolsSetting();
        setting.setItem(activeProtocol);
        setting.setItem(inactiveProtocol);

        List<EzyProtocolSetting> activeProtocols = setting.getActiveProtocols();
        assert setting.getProtocols().size() == 2;
        assert activeProtocols.size() == 1;
        assert activeProtocols.get(0) == activeProtocol;
    }

    @Test
    public void hasActiveProtocolTest() {
        EzySimpleProtocolsSetting setting = new EzySimpleProtocolsSetting();
        assert !setting.hasActiveProtocol();

        EzySimpleProtocolSetting inactiveProtocol = new EzySimpleProtocolSetting();
        inactiveProtocol.setActive(false);
        setting.setItem(inactiveProtocol);
        assert !setting.hasActiveProtocol();

        setting.setItem(new EzySimpleProtocolSetting());
        assert setting.hasActiveProtocol();
    }
}

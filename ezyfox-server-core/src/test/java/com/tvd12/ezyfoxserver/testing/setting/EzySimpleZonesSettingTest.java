package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleZonesSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleZonesSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleZonesSetting zonesSetting = new EzySimpleZonesSetting();
        try {
            zonesSetting.getZoneByName("no zone");
        }
        catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            zonesSetting.getZoneById(-1);
        }
        catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        assert zonesSetting.getSize() == 0;
    }

}

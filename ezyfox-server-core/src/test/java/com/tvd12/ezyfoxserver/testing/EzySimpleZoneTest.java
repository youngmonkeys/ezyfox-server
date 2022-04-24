package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleZoneTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleZone zone = new EzySimpleZone();
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        zone.setSetting(setting);
        assert zone.equals(zone);
        assert !zone.equals(new EzySimpleZone());
    }

}

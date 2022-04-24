package com.tvd12.ezyfoxserver.testing.context;

import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContexts;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyZoneContextsTest extends BaseTest {

    @Test
    public void test() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzySimpleZone zone = new EzySimpleZone();
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zone.setSetting(zoneSetting);
        assert EzyZoneContexts.getUserManagementSetting(zoneContext) == zoneSetting.getUserManagement();
    }

    @Override
    public Class<?> getTestClass() {
        return EzyZoneContexts.class;
    }
}

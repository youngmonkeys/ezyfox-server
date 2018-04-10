package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;

public final class EzyZoneContextsTest {

    private EzyZoneContextsTest() {
    }
    
    public static EzyZoneContext newDefaultZoneContext() {
        EzySimpleZoneContext context = new EzySimpleZoneContext();
        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(new EzySimpleZoneSetting());
        context.setZone(zone);
        return context;
    }
    
}

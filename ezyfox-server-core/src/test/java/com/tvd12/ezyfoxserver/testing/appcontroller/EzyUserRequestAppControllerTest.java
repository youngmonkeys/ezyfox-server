package com.tvd12.ezyfoxserver.testing.appcontroller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.appcontroller.EzyUserRequestAppController;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyUserRequestAppControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUserRequestAppController ctrl = new EzyUserRequestAppController();
        EzyServerContext ctx = newServerContext();
        EzyUserRequestAppEvent event = new EzySimpleUserRequestAppEvent(
                newUser(),
                null,
                newArrayBuilder().build());
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        ctrl.handle(zoneContext.getAppContext("ezyfox-chat"), event);
    }
    
}

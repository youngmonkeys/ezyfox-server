package com.tvd12.ezyfoxserver.testing.appcontroller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.appcontroller.EzyUserRemovedController;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyUserDisconnectControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUserRemovedController ctrl = new EzyUserRemovedController();
        EzyServerContext ctx = newServerContext();
        EzyUserRemovedEvent event = (EzyUserRemovedEvent) EzySimpleUserRemovedEvent.builder()
                .reason(EzyDisconnectReason.IDLE)
                .user(newUser())
                .build();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        ctrl.handle(zoneContext.getAppContext("ezyfox-chat"), event);
    }
    
}

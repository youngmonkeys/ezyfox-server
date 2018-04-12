package com.tvd12.ezyfoxserver.testing.appcontroller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.appcontroller.EzyUserDisconnectController;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzySimpleUserDisconnectEvent;
import com.tvd12.ezyfoxserver.event.EzyUserDisconnectEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyUserDisconnectControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUserDisconnectController ctrl = new EzyUserDisconnectController();
        EzyServerContext ctx = newServerContext();
        EzyUserDisconnectEvent event = (EzyUserDisconnectEvent) EzySimpleUserDisconnectEvent.builder()
                .reason(EzyDisconnectReason.IDLE)
                .user(newUser())
                .build();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        ctrl.handle(zoneContext.getAppContext("ezyfox-chat"), event);
    }
    
}

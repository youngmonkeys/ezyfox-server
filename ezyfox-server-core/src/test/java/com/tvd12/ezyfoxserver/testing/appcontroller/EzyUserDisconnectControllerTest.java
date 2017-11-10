package com.tvd12.ezyfoxserver.testing.appcontroller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.appcontroller.EzyUserDisconnectController;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyUserDisconnectEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserDisconnectEventImpl;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyUserDisconnectControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUserDisconnectController ctrl = new EzyUserDisconnectController();
        EzyServerContext ctx = newServerContext();
        EzyUserDisconnectEvent event = (EzyUserDisconnectEvent) EzyUserDisconnectEventImpl.builder()
                .reason(EzyDisconnectReason.IDLE)
                .user(newUser())
                .build();
        ctrl.handle(ctx.getAppContext("ezyfox-chat"), event);
    }
    
}

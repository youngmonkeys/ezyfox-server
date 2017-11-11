package com.tvd12.ezyfoxserver.testing.appcontroller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.appcontroller.EzyUserRequestAppController;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleUserRequestAppEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyUserRequestAppControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUserRequestAppController ctrl = new EzyUserRequestAppController();
        EzyServerContext ctx = newServerContext();
        EzyUserRequestAppEvent event = (EzyUserRequestAppEvent) EzySimpleUserRequestAppEvent.builder()
                .data(newArrayBuilder().build())
                .user(newUser())
                .build();
        ctrl.handle(ctx.getAppContext("ezyfox-chat"), event);
    }
    
}

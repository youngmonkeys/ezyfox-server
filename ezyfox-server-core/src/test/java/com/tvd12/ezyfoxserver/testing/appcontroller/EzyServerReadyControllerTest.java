package com.tvd12.ezyfoxserver.testing.appcontroller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.appcontroller.EzyServerReadyController;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyServerReadyControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyServerReadyController ctrl = new EzyServerReadyController();
        EzyServerContext ctx = newServerContext();
        EzyServerReadyEvent event = EzySimpleServerReadyEvent.builder()
                .build();
        ctrl.handle(ctx.getAppContext("ezyfox-chat"), event);
    }
    
}

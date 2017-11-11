package com.tvd12.ezyfoxserver.testing.plugincontroller;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.plugincontroller.EzyServerReadyController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyServerReadyControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyServerReadyEvent event = EzySimpleServerReadyEvent.builder()
                .build();
        EzyPluginContext context = Mockito.mock(EzyPluginContext.class);
        EzyServerReadyController controller = new EzyServerReadyController();
        controller.handle(context, event);
    }
    
}

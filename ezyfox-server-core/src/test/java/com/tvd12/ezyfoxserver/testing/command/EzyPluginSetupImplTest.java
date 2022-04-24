package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginSetupImpl;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyPluginSetupImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzyPluginSetupImpl cmd = new EzyPluginSetupImpl(plugin);
        cmd.setRequestController(new EzyPluginRequestController() {
            @Override
            public void handle(EzyPluginContext ctx, EzyUserRequestPluginEvent event) {
            }
        });
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void addEventControllerTest() {
        // given
        EzySimplePlugin plugin = new EzySimplePlugin();

        EzyEventControllers eventControllers = mock(EzyEventControllers.class);
        plugin.setEventControllers(eventControllers);

        EzyPluginSetupImpl sut = new EzyPluginSetupImpl(plugin);
        EzyEventController controller = mock(EzyEventController.class);


        // when
        sut.addEventController(EzyEventType.SERVER_INITIALIZING, controller);

        // then
        verify(eventControllers, times(1)).addController(EzyEventType.SERVER_INITIALIZING, controller);
    }
}

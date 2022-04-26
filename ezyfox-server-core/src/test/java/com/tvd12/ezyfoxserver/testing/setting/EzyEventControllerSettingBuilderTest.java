package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyEventControllerSetting;
import com.tvd12.ezyfoxserver.setting.EzyEventControllerSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyEventControllerSettingBuilderTest {

    @Test
    public void test() {
        // given
        EzyEventControllerSettingBuilder sut = new EzyEventControllerSettingBuilder()
            .controller(A.class)
            .eventType(EzyEventType.SERVER_READY);

        // when
        EzyEventControllerSetting eventController = sut.build();

        // then
        Asserts.assertEquals(A.class.getName(), eventController.getController());
        Asserts.assertEquals(EzyEventType.SERVER_READY.toString(), eventController.getEventType());
    }

    private static class A implements EzyEventController<EzyServerContext, EzyEvent> {
        @Override
        public void handle(EzyServerContext ctx, EzyEvent event) {}
    }
}

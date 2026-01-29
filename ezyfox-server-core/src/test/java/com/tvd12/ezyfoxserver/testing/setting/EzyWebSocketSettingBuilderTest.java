package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyWebSocketSettingBuilderTest {

    @Test
    public void test() {
        // given
        EzyWebSocketSettingBuilder sut = new EzyWebSocketSettingBuilder()
            .managementEnable(true);

        // when
        EzySimpleWebSocketSetting setting = sut.build();

        // then
        Asserts.assertEquals(true, setting.isManagementEnable());
    }

    @Test
    public void testHandlerThreadPoolSize() {
        // given
        EzyWebSocketSettingBuilder sut = new EzyWebSocketSettingBuilder()
            .minHandlerThreadPoolSize(2)
            .maxHandlerThreadPoolSize(5);

        // when
        EzySimpleWebSocketSetting setting = sut.build();

        // then
        Asserts.assertEquals(setting.getMinHandlerThreadPoolSize(), 2);
        Asserts.assertEquals(setting.getMaxHandlerThreadPoolSize(), 5);
    }
}

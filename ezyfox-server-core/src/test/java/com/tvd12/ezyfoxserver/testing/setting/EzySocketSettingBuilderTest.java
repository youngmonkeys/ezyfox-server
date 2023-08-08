package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.constant.SslType;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

public class EzySocketSettingBuilderTest {

    @Test
    public void test() {
        // given
        SslType sslType = RandomUtil.randomEnumValue(
            SslType.class
        );
        int sslHandshakeTimeout = RandomUtil.randomInt();

        // when
        EzySocketSetting setting = new EzySocketSettingBuilder()
            .sslType(sslType)
            .sslHandshakeTimeout(sslHandshakeTimeout)
            .build();

        // then
        Asserts.assertEquals(setting.getSslType(), sslType);
        Asserts.assertEquals(
            setting.getSslHandshakeTimeout(),
            sslHandshakeTimeout
        );
    }
}

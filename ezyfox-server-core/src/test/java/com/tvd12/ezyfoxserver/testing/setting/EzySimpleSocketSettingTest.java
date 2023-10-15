package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.constant.SslType;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleSocketSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setPort(123);
        assert setting.getPort() == 123;
        setting.setAddress("127.0.0.1");
        assert setting.getAddress().equals("127.0.0.1");
        setting.setActive(true);
        assert setting.isActive();
        setting.setCodecCreator("hello");
        assert setting.getCodecCreator().equals("hello");
        setting.setTcpNoDelay(true);
        assert setting.isTcpNoDelay();
    }

    @Test
    public void isL4SslActiveTrueTest() {
        // given
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setSslActive(true);
        setting.setSslType(SslType.CERTIFICATION);

        // when
        // then
        Asserts.assertTrue(setting.isCertificationSslActive());
    }

    @Test
    public void isL4SslActiveFalseDueToSslEnableTest() {
        // given
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setSslActive(false);
        setting.setSslType(SslType.CERTIFICATION);

        // when
        // then
        Asserts.assertFalse(setting.isCertificationSslActive());
    }

    @Test
    public void isL4SslActiveFalseDueToSslTypeTest() {
        // given
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setSslActive(true);
        setting.setSslType(SslType.CUSTOMIZATION);

        // when
        // then
        Asserts.assertFalse(setting.isCertificationSslActive());
    }

    @Test
    public void isL7SslActiveTrueTest() {
        // given
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setSslActive(true);
        setting.setSslType(SslType.CUSTOMIZATION);

        // when
        // then
        Asserts.assertTrue(setting.isCustomizationSslActive());
    }

    @Test
    public void isL7SslActiveFalseDueToSslEnableTest() {
        // given
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setSslActive(false);
        setting.setSslType(SslType.CUSTOMIZATION);

        // when
        // then
        Asserts.assertFalse(setting.isCustomizationSslActive());
    }

    @Test
    public void isL7SslActiveFalseDueToSslTypeTest() {
        // given
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setSslActive(true);
        setting.setSslType(SslType.CERTIFICATION);

        // when
        // then
        Asserts.assertFalse(setting.isCustomizationSslActive());
    }
}

package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzySimpleSettingsReaderTest extends BaseTest {

    @Test
    public void test() {
        // given
        String homePath = "test-data";
        EzySettingsDecorator settingsDecorator = mock(EzySettingsDecorator.class);

        EzySettingsReader sut = EzySimpleSettingsReader.builder()
            .homePath(homePath)
            .classLoader(Thread.currentThread().getContextClassLoader())
            .settingsDecorator(settingsDecorator)
            .build();

        // when
        EzySettings settings = sut.read();

        // then
        verify(settingsDecorator, times(1)).decorate(homePath, (EzySimpleSettings) settings);
    }
}

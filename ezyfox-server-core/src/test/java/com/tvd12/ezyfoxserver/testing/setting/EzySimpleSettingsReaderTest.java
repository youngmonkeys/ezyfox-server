package com.tvd12.ezyfoxserver.testing.setting;

import static org.mockito.Mockito.*;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySettingsDecorator;
import com.tvd12.ezyfoxserver.setting.EzySettingsReader;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettingsReader;
import com.tvd12.test.base.BaseTest;

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

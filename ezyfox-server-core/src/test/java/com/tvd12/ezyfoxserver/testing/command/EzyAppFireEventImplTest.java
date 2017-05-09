package com.tvd12.ezyfoxserver.testing.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.command.impl.EzyAppFireEventImpl;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyAppFireEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleAppSetting app = mock(EzySimpleAppSetting.class);
        EzyConstant type = EzyConstant.one();
        EzyEventControllers ctrs = mock(EzyEventControllers.class);
        EzyAppContext context = mock(EzyAppContext.class);
        when(app.getEventControllers()).thenReturn(ctrs);
        
        EzySimpleApplication application = new EzySimpleApplication();
        application.setSetting(app);
        
        when(context.getApp()).thenReturn(application);
        EzyAppFireEventImpl event = new EzyAppFireEventImpl(context);
        event.fire(type, null);
    }
    
}

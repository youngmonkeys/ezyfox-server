package com.tvd12.ezyfoxserver.testing.delegate;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySimpleAppUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzySimpleAppUserDelegateTest extends BaseTest {

    @Test
    public void test() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        when(appContext.getApp()).thenReturn(app);
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getParent()).thenReturn(zoneContext);
        EzySimpleAppUserDelegate delegate = new EzySimpleAppUserDelegate();
        delegate.setAppContext(appContext);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        delegate.onUserRemoved(user, EzyUserRemoveReason.EXIT_APP);
    }

    @Test
    public void testExceptionCase() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        when(appContext.getApp()).thenReturn(app);
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getParent()).thenReturn(zoneContext);

        doThrow(new IllegalStateException()).when(appContext).handleEvent(any(), any());

        EzySimpleAppUserDelegate delegate = new EzySimpleAppUserDelegate();
        delegate.setAppContext(appContext);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        delegate.onUserRemoved(user, EzyUserRemoveReason.EXIT_APP);
    }
}

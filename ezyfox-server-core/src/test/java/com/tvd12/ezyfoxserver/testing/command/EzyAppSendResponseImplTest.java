package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.impl.EzyAppSendResponseImpl;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyAppSendResponseImplTest extends BaseTest {

    @Test
    public void test() {
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        when(appContext.getApp()).thenReturn(app);
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("test");
        when(app.getSetting()).thenReturn(appSetting);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(appContext.getParent()).thenReturn(zoneContext);
        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(zoneContext.getParent()).thenReturn(serverContext);
        EzyAppSendResponseImpl cmd = new EzyAppSendResponseImpl(appContext);

        EzyData data = EzyEntityFactory.newArrayBuilder()
            .build();
        EzyAbstractSession session = spy(EzyAbstractSession.class);

        cmd.execute(data, session, false);
        cmd.execute(data, Lists.newArrayList(session), false);
    }
}

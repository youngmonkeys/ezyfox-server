package com.tvd12.ezyfoxserver.testing.controller;

import static org.mockito.Mockito.*;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.request.EzySimpleAccessAppRequest;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.test.base.BaseTest;

public class EzyAccessAppControllerTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        when(appContext.getApp()).thenReturn(app);
        EzyAppUserManager appUserManager = mock(EzyAppUserManager.class);
        when(appUserManager.getMaxUsers()).thenReturn(2);
        when(app.getUserManager()).thenReturn(appUserManager);
        when(zoneContext.getAppContext("test")).thenReturn(appContext);

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("user");
        user.setZoneId(1);
        EzyArray params = EzyEntityFactory.newArrayBuilder()
                .append("test")
                .build();
        EzySimpleAccessAppRequest request = new EzySimpleAccessAppRequest();
        request.setUser(user);
        request.setSession(session);
        request.deserializeParams(params);
        
        EzyAccessAppController controller = new EzyAccessAppController();
        controller.handle(serverContext, request);
    }
    
}

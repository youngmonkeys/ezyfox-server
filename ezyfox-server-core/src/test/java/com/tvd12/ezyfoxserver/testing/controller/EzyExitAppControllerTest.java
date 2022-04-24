package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyExitAppController;
import com.tvd12.ezyfoxserver.request.EzySimpleExitAppRequest;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyExitAppControllerTest extends BaseTest {

    @Test
    public void test() {
        EzyExitAppController controller = new EzyExitAppController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        when(appContext.getApp()).thenReturn(app);
        EzyAppUserManager userManager = mock(EzyAppUserManager.class);
        when(app.getUserManager()).thenReturn(userManager);
        when(serverContext.getAppContext(1)).thenReturn(appContext);
        EzySimpleExitAppRequest request = new EzySimpleExitAppRequest();
        request.deserializeParams(EzyEntityFactory.newArrayBuilder()
            .append(1)
            .build());
        controller.handle(serverContext, request);
    }

}

package com.tvd12.ezyfoxserver.testing.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.delegate.EzySimpleAppUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.exception.EzyAccessAppException;
import com.tvd12.ezyfoxserver.request.EzySimpleAccessAppRequest;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyAccessAppControllerTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("test");
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getApp()).thenReturn(app);
        EzySimpleAppUserDelegate userDelegate = new EzySimpleAppUserDelegate();
        userDelegate.setAppContext(appContext);
        EzyAppUserManager appUserManager = EzyAppUserManagerImpl.builder()
                .maxUsers(2)
                .appName("test")
                .userDelegate(userDelegate)
                .build();
        when(app.getUserManager()).thenReturn(appUserManager);
        when(zoneContext.getAppContext("test")).thenReturn(appContext);

        EzySimpleAccessAppRequest request1 = newRequest(1);
        
        EzyAccessAppController controller = new EzyAccessAppController();
        controller.handle(serverContext, request1);
        controller.handle(serverContext, request1);
        
        EzySimpleAccessAppRequest request2 = newRequest(2);
        controller.handle(serverContext, request2);
        
        try {
            EzySimpleAccessAppRequest request3 = newRequest(3);
            controller.handle(serverContext, request3);
        }
        catch(Exception e) {
            assert e instanceof EzyAccessAppException;
        }
        
        try {
            EzySimpleAccessAppRequest request4 = newRequest(4);
            MethodInvoker.create()
                .object(controller)
                .method("addUser")
                .param(EzyAppUserManager.class, app.getUserManager())
                .param(EzyUser.class, request4.getUser())
                .param(EzyAppSetting.class, app.getSetting())
                .invoke();
        }
        catch(Exception e) {
            assert e.getCause().getCause() instanceof EzyAccessAppException;
        }
    }
    
    protected EzySimpleAccessAppRequest newRequest(int index) {
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("user" + index);
        user.setZoneId(1);
        EzyArray params = EzyEntityFactory.newArrayBuilder()
                .append("test")
                .build();
        EzySimpleAccessAppRequest request = new EzySimpleAccessAppRequest();
        request.setUser(user);
        request.setSession(session);
        request.deserializeParams(params);
        return request;
    }
    
}

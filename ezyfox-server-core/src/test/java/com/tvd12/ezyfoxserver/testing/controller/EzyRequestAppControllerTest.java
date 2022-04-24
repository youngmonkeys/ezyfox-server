package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyRequestAppController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreContextTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyRequestAppControllerTest extends BaseCoreContextTest {

    private MyTestUser user;
    private EzySession session;

    @Test
    public void test() {
        EzyArray data = EzyEntityArrays.newArray(1, EzyEntityArrays.newArray());
        EzyRequestAppController controller = new EzyRequestAppController();
        EzySimpleRequestAppRequest request = new EzySimpleRequestAppRequest();
        request.deserializeParams(data);
        request.setUser(user);
        request.setSession(session);
        EzyServerContext ctx = newServerContext();
        controller.handle(ctx, request);
    }

    @Test
    public void testHasNotAccessed() {
        EzyArray data = EzyEntityArrays.newArray(1, EzyEntityArrays.newArray());
        EzyRequestAppController controller = new EzyRequestAppController();
        MyTestUser user = new MyTestUser();
        user.setName("dungtv1");
        user.addSession(session);
        EzySimpleRequestAppRequest request = new EzySimpleRequestAppRequest();
        request.deserializeParams(data);
        request.setUser(user);
        request.setSession(session);
        EzyServerContext ctx = newServerContext();
        controller.handle(ctx, request);
    }

    @Override
    protected EzyServerContext newServerContext() {
        session = newSession();
        session.setToken("abc");
        user = new MyTestUser();
        user.setName("dungtv");
        user.addSession(session);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzySimpleApplication app = new EzySimpleApplication();
        app.setSetting(new EzySimpleAppSetting());
        app.setUserManager(EzyAppUserManagerImpl.builder().build());
        app.getUserManager().addUser(user);

        when(appContext.getApp()).thenReturn(app);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getAppContext(1)).thenReturn(appContext);
        return serverContext;
    }
}

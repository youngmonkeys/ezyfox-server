package com.tvd12.ezyfoxserver.testing.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyRequestAppController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;
import com.tvd12.ezyfoxserver.request.EzyRequestAppRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreContextTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;

public class EzyRequestAppControllerTest extends BaseCoreContextTest {

    private MyTestUser user;
    private EzySession session;
    
    @Test
    public void test() {
        EzyRequestAppParams data = EzySimpleRequestAppParams.builder()
                .appId(1)
                .data(newArrayBuilder().build())
                .build();
        EzyRequestAppController controller = new EzyRequestAppController();
        EzyRequestAppRequest request = EzySimpleRequestAppRequest.builder()
                .params(data)
                .user(user)
                .session(session)
                .build();
        EzyServerContext ctx = newServerContext();
        controller.handle(ctx, request);
    }
    
    @Override
    protected EzyServerContext newServerContext() {
        session = newSession();
        session.setReconnectToken("abc");
        user = new MyTestUser();
        user.setName("dungtv");
        user.addSession(session);
        EzyAppContext appContext = mock(EzyAppContext.class);
        when(appContext.get(EzyFireEvent.class)).thenReturn(new EzyFireEvent() {
            
            @Override
            public void fire(EzyConstant type, EzyEvent event) {
                
            }
        });
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

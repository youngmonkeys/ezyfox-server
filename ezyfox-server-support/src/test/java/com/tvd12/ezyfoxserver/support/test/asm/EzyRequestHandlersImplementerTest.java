package com.tvd12.ezyfoxserver.support.test.asm;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserSessionEvent;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.asm.EzyAsmRequestHandler;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlerImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyRequestHandlersImplementer;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;
import com.tvd12.ezyfoxserver.support.exception.EzyDuplicateRequestHandlerException;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController2;
import com.tvd12.ezyfoxserver.support.test.data.GreetRequest;
import com.tvd12.ezyfoxserver.support.test.data.GreetResponse;
import com.tvd12.test.assertion.Asserts;

public class EzyRequestHandlersImplementerTest {

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void test() {
        // given
        EzyAppContext context = mock(EzyAppContext.class);
        EzySession session = mock(EzyAbstractSession.class);
        EzyUser user = new EzySimpleUser();
        EzyUserSessionEvent event = new EzySimpleUserSessionEvent(user, session);
        EzyRequestHandlerImplementer.setDebug(true);
        EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();

        EzyResponseFactory responseFactory = mock(EzyResponseFactory.class);
        EzyObjectResponse objectResponse = mock(EzyObjectResponse.class);
        when(responseFactory.newObjectResponse()).thenReturn(objectResponse);
        when(objectResponse.command("Big/Hello6")).thenReturn(objectResponse);
        when(objectResponse.data(new GreetResponse("Hello Dzung!"))).thenReturn(objectResponse);
        when(objectResponse.session(any())).thenReturn(objectResponse);
        doNothing().when(objectResponse).execute();

        implementer.setResponseFactory(responseFactory);

        EzyFeatureCommandManager featureCommandManager = new EzyFeatureCommandManager();
        EzyRequestCommandManager requestCommandManager = new EzyRequestCommandManager();
        
        implementer.setFeatureCommandManager(featureCommandManager);
        implementer.setRequestCommandManager(requestCommandManager);

        Map<String, EzyUserRequestHandler> handlers = implementer.implement(
            Collections.singletonList(new HelloController())
        );
        for(EzyUserRequestHandler handler : handlers.values()) {
            handler.handle(context, event, new GreetRequest("Dzung"));
        }
        EzyRequestHandlerImplementer.setDebug(false);
        implementer = new EzyRequestHandlersImplementer();
        implementer.setFeatureCommandManager(featureCommandManager);
        implementer.setRequestCommandManager(requestCommandManager);

        // when
        handlers = implementer.implement(Collections.singletonList(new HelloController()));

        // then
        Asserts.assertTrue(handlers.containsKey("Big/Hello"));
        verify(responseFactory, times(1)).newObjectResponse();
        verify(objectResponse, times(1)).command("Big/Hello6");
        verify(objectResponse, times(1)).data(new GreetResponse("Hello Dzung!"));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testImplementFailedCase() {
        EzyRequestControllerProxy proxy
            = new EzyRequestControllerProxy(new HelloController());
        EzyRequestHandlerImplementer implementer =
                new EzyRequestHandlerImplementer(
                        proxy,
                        proxy.getRequestHandlerMethods().get(0)) {
            @Override
            protected EzyAsmRequestHandler doimplement() throws Exception {
                throw new RuntimeException("test");
            }
        };
        implementer.implement();
    }

    @Test
    public void testImplementFailedCase2() {
        EzyRequestHandlerImplementer.setDebug(true);
        EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
        EzyFeatureCommandManager featureCommandManager = new EzyFeatureCommandManager();
        EzyRequestCommandManager requestCommandManager = new EzyRequestCommandManager();
        implementer.setFeatureCommandManager(featureCommandManager);
        implementer.setRequestCommandManager(requestCommandManager);
        implementer.implement(Collections.singletonList(new HelloController2()));
    }

    @Test(expectedExceptions = EzyDuplicateRequestHandlerException.class)
    public void testImplementFailedCase3() {
        EzyRequestHandlerImplementer.setDebug(true);
        EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
        EzyFeatureCommandManager featureCommandManager = new EzyFeatureCommandManager();
        EzyRequestCommandManager requestCommandManager = new EzyRequestCommandManager();
        implementer.setFeatureCommandManager(featureCommandManager);
        implementer.setRequestCommandManager(requestCommandManager);
        implementer.implement(Arrays.asList(new HelloController(), new HelloController()));
    }

    @Test
    public void testImplementDuplicateCommandButAllowOverride() {
        EzyRequestHandlerImplementer.setDebug(true);
        EzyRequestHandlersImplementer implementer = new EzyRequestHandlersImplementer();
        EzyFeatureCommandManager featureCommandManager = new EzyFeatureCommandManager();
        EzyRequestCommandManager requestCommandManager = new EzyRequestCommandManager();
        implementer.setFeatureCommandManager(featureCommandManager);
        implementer.setRequestCommandManager(requestCommandManager);
        implementer.setAllowOverrideCommand(true);
        implementer.implement(Arrays.asList(new HelloController(), new HelloController()));
    }
}

package com.tvd12.ezyfoxserver.support.test.controller;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPrototypeFactory;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPrototypeController;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class EzyUserRequestPrototypeControllerTest {

    @Test
    public void test() {
        // given
        EzyPrototypeFactory prototypeFactory = mock(EzyPrototypeFactory.class);
        when(prototypeFactory.getSuppliers(EzyRequestListener.class))
            .thenReturn(Collections.emptyList());

        EzyBeanContext beanContext = mock(EzyBeanContext.class);
        when(beanContext.getPrototypeFactory()).thenReturn(prototypeFactory);

        InternalController sut = new InternalController.Builder()
            .beanContext(beanContext)
            .build();

        // when
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyUserRequestEvent event = mock(EzyUserRequestEvent.class);
        String cmd = RandomUtil.randomShortAlphabetString();
        EzyHandler handler = mock(EzyHandler.class);
        sut.postHandle(appContext, event, cmd, handler);

        // then
        verify(beanContext, times(1)).getPrototypeFactory();
        verify(beanContext, times(1)).getSingleton(
            "unmarshaller", EzyUnmarshaller.class
        );
    }

    public static class InternalController
        extends EzyUserRequestPrototypeController<EzyAppContext, EzyUserRequestEvent> {

        public InternalController(Builder builder) {
            super(builder);
        }

        @Override
        protected void preHandle(
            EzyAppContext context,
            EzyUserRequestEvent event,
            String cmd,
            EzyHandler handler
        ) {}

        @Override
        protected void responseError(
            EzyAppContext context,
            EzyUserRequestEvent event,
            EzyData errorData
        ) {}

        @Override
        public void postHandle(
            EzyAppContext context,
            EzyUserRequestEvent event,
            String cmd,
            EzyHandler handler,
            Exception e
        ) {
            super.postHandle(context, event, cmd, handler, e);
        }

        @Override
        public void postHandle(
            EzyAppContext context,
            EzyUserRequestEvent event,
            String cmd,
            EzyHandler handler
        ) {
            super.postHandle(context, event, cmd, handler);
        }

        public static class Builder
            extends EzyUserRequestPrototypeController.Builder<Builder> {
            @Override
            public InternalController build() {
                return new InternalController(this);
            }
        }
    }
}

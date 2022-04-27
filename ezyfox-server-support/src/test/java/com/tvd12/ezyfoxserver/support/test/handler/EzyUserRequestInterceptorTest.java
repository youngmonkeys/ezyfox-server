package com.tvd12.ezyfoxserver.support.test.handler;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestInterceptor;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzyUserRequestInterceptorTest {

    @Test
    public void preHandleTest() {
        // given
        EzyUserRequestInterceptor<EzyContext> sut
            = new EzyUserRequestInterceptor<EzyContext>() {};

        // when
        // then
        EzyContext context = mock(EzyContext.class);
        EzyUserSessionEvent event = mock(EzyUserSessionEvent.class);
        String command = RandomUtil.randomShortAlphabetString();
        String data = RandomUtil.randomShortAlphabetString();
        sut.preHandle(
            context,
            event,
            data,
            command
        );
        sut.postHandle(
            context,
            event,
            data,
            command
        );
        Exception e = new Exception("just test");
        sut.postHandle(
            context,
            event,
            data,
            command,
            e
        );
    }
}

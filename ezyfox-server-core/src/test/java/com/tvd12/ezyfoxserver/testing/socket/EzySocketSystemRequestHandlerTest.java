package com.tvd12.ezyfoxserver.testing.socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandler;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketSystemRequestHandlerTest {

    @Test
    public void test() {
        EzySocketSystemRequestHandler handler = new EzySocketSystemRequestHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getRequestType")
            .invoke();
    }

    @Test
    public void getRequestQueueTest() {
        // given
        EzySocketSystemRequestHandler sut = new EzySocketSystemRequestHandler();

        EzySession session = mock(EzySession.class);
        EzyRequestQueue requestQueue = mock(EzyRequestQueue.class);
        when(session.getSystemRequestQueue()).thenReturn(requestQueue);

        // when
        EzyRequestQueue result = MethodInvoker.create()
                .object(sut)
                .method("getRequestQueue")
                .param(EzySession.class, session)
                .call();

        // then
        Asserts.assertEquals(requestQueue, result);
    }
}

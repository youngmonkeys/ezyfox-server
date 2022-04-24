package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandler;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import static org.mockito.Mockito.*;

public class EzySocketExtensionRequestHandlerTest {

    @Test
    public void test() {
        EzySocketExtensionRequestHandler handler = new EzySocketExtensionRequestHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getRequestType")
            .invoke();
    }

    @Test
    public void getRequestQueueTest() {
        // given
        EzySocketExtensionRequestHandler sut = new EzySocketExtensionRequestHandler();

        EzySession session = mock(EzySession.class);
        EzyRequestQueue requestQueue = mock(EzyRequestQueue.class);
        when(session.getExtensionRequestQueue()).thenReturn(requestQueue);

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

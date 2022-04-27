package com.tvd12.ezyfoxserver.support.test.asm;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.asm.EzyAsmAbstractRequestHandler;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyAsmAbstractRequestHandlerTest {

    @Test
    public void responseToSessionTest() {
        // given
        String command = RandomUtil.randomShortAlphabetString();
        String data = RandomUtil.randomShortAlphabetString();
        EzyUserSessionEvent event = mock(EzyUserSessionEvent.class);

        EzySession session = mock(EzySession.class);
        when(event.getSession()).thenReturn(session);

        EzyObjectResponse objectResponse = mock(EzyObjectResponse.class);
        when(objectResponse.command(command)).thenReturn(objectResponse);
        when(objectResponse.data(data)).thenReturn(objectResponse);
        when(objectResponse.session(session)).thenReturn(objectResponse);

        EzyResponseFactory responseFactory = mock(EzyResponseFactory.class);
        when(responseFactory.newObjectResponse()).thenReturn(objectResponse);

        InternalRequestHandler sut = new InternalRequestHandler();
        sut.setCommand(command);
        sut.setResponseFactory(responseFactory);

        // then
        sut.responseToSession(event, data);

        // then
        verify(event, times(1)).getSession();
        verify(objectResponse, times(1)).command(command);
        verify(objectResponse, times(1)).data(data);
        verify(objectResponse, times(1)).session(session);
        verify(objectResponse, times(1)).execute();
        verify(responseFactory, times(1)).newObjectResponse();
    }

    private static class InternalRequestHandler
        extends EzyAsmAbstractRequestHandler {

        @Override
        public void handleRequest(
            EzyContext context,
            EzyUserSessionEvent event,
            Object data
        ) {}

        @Override
        public void handleException(
            EzyContext context,
            EzyUserSessionEvent event,
            Object data,
            Exception exception
        ) {}

        @Override
        public void responseToSession(
            EzyUserSessionEvent event,
            Object data
        ) {
            super.responseToSession(event, data);
        }

        @Override
        public void setController(Object controller) {}
    }
}

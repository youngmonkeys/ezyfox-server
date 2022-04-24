package com.tvd12.ezyfoxserver.testing.exception;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyRequestHandleException;
import com.tvd12.test.base.BaseTest;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class EzyRequestHandleExceptionTest extends BaseTest {

    @Test(expectedExceptions = {EzyRequestHandleException.class})
    public void test() {
        EzySession session = Mockito.mock(EzySession.class);
        Mockito.when(session.getName()).thenReturn("hello world");
        throw EzyRequestHandleException
            .requestHandleException(session, EzyCommand.LOGIN, new Object(), new Exception());

    }

    @Test
    public void test2() {
        EzySession session = Mockito.mock(EzySession.class);
        Mockito.when(session.getName()).thenReturn("hello world");
        EzyRequestHandleException exception = EzyRequestHandleException
            .requestHandleException(session, EzyCommand.LOGIN, new Object(), new Exception());
        assert exception.getSession() == session;
        assert exception.getCommand() == EzyCommand.LOGIN;
        assert exception.getData() != null;

    }
}

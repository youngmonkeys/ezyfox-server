package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.command.impl.EzyHandleExceptionImpl;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyHandleExceptionImplTest extends BaseTest {

    @Test
    public void exceptionCaseTest() {
        EzyExceptionHandlersFetcher fetcher = mock(EzyExceptionHandlersFetcher.class);
        EzyExceptionHandlers handlers = mock(EzyExceptionHandlers.class);
        when(fetcher.getExceptionHandlers()).thenReturn(handlers);
        doThrow(new IllegalStateException("server maintain")).when(handlers).handleException(any(), any());
        EzyHandleExceptionImpl cmd = new EzyHandleExceptionImpl(fetcher);
        cmd.handle(Thread.currentThread(), new Exception());
    }

}

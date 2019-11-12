package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.command.impl.EzyAddExceptionHandlerImpl;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzyAddExceptionHandlerImplTest extends BaseTest {
    
    @Test
    public void test() {
        EzyExceptionHandlersFetcher fetcher = mock(EzyExceptionHandlersFetcher.class);
        EzyExceptionHandlers handlers = mock(EzyExceptionHandlers.class);
        when(fetcher.getExceptionHandlers()).thenReturn(handlers);
        EzyAddExceptionHandlerImpl cmd = new EzyAddExceptionHandlerImpl(fetcher);
        cmd.add(mock(EzyExceptionHandler.class));
    }

}

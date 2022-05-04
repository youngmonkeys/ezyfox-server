package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.command.impl.EzyHandleExceptionImpl;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import static com.tvd12.ezyfox.io.EzyStrings.exceptionToSimpleString;
import static org.mockito.Mockito.*;

public class EzyHandleExceptionImplTest extends BaseTest {

    @Test
    public void handleExceptionWithEmptyHandlers() {
        // given
        EzyExceptionHandlersFetcher fetcher = mock(EzyExceptionHandlersFetcher.class);
        EzyExceptionHandlers handlers = mock(EzyExceptionHandlers.class);
        when(handlers.isEmpty()).thenReturn(true);

        when(fetcher.getExceptionHandlers()).thenReturn(handlers);

        EzyHandleExceptionImpl sut = new EzyHandleExceptionImpl(fetcher);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        // when
        Exception exception = new IllegalArgumentException("one");
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(handlers, times(1)).isEmpty();
        verify(logger, times(1)).info(
            "there is no handler for exception: ",
            exception
        );
    }

    @Test
    public void handleExceptionWithHandlers() {
        // given
        EzyExceptionHandlersFetcher fetcher = mock(EzyExceptionHandlersFetcher.class);

        Exception exception = new IllegalArgumentException("one");

        EzyExceptionHandlers handlers = mock(EzyExceptionHandlers.class);
        when(fetcher.getExceptionHandlers()).thenReturn(handlers);

        EzyHandleExceptionImpl sut = new EzyHandleExceptionImpl(fetcher);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        // when
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(handlers, times(1)).isEmpty();
        verify(handlers, times(1)).handleException(
            Thread.currentThread(),
            exception
        );

        verify(logger, times(0)).info(
            "there is no handler for exception: ",
            exception
        );
    }

    @Test
    public void handleExceptionWithHandlersButException() {
        // given
        EzyExceptionHandlersFetcher fetcher = mock(EzyExceptionHandlersFetcher.class);

        Exception exception = new IllegalArgumentException("one");

        EzyExceptionHandlers handlers = mock(EzyExceptionHandlers.class);
        when(fetcher.getExceptionHandlers()).thenReturn(handlers);

        EzyHandleExceptionImpl sut = new EzyHandleExceptionImpl(fetcher);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        RuntimeException ex = new RuntimeException("just test");
        doThrow(ex).when(handlers).handleException(
            Thread.currentThread(),
            exception
        );

        // when
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(handlers, times(1)).isEmpty();
        verify(handlers, times(1)).handleException(
            Thread.currentThread(),
            exception
        );

        verify(logger, times(1)).warn(
            "handle exception: {} error",
            exceptionToSimpleString(exception),
            ex
        );
    }
}

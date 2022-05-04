package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.command.impl.EzyAppHandleExceptionImpl;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import static com.tvd12.ezyfox.io.EzyStrings.exceptionToSimpleString;
import static org.mockito.Mockito.*;

public class EzyAppHandleExceptionImplTest extends BaseTest {

    @Test
    public void handleExceptionWithEmptyHandlers() {
        // given
        EzySimpleApplication app = new EzySimpleApplication();

        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        String appName = RandomUtil.randomShortAlphabetString();
        setting.setName(appName);
        app.setSetting(setting);

        EzyAppHandleExceptionImpl sut = new EzyAppHandleExceptionImpl(app);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        // when
        Exception exception = new IllegalArgumentException("one");
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(logger, times(1)).info(
            "app: {} has no handler for exception:",
            appName,
            exception
        );
    }

    @Test
    public void handleExceptionWithHandlers() {
        // given
        EzySimpleApplication app = new EzySimpleApplication();

        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        String appName = RandomUtil.randomShortAlphabetString();
        setting.setName(appName);
        app.setSetting(setting);

        EzyAppHandleExceptionImpl sut = new EzyAppHandleExceptionImpl(app);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        EzyExceptionHandler exceptionHandler = mock(EzyExceptionHandler.class);
        app.getExceptionHandlers().addExceptionHandler(exceptionHandler);

        // when
        Exception exception = new IllegalArgumentException("one");
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(exceptionHandler, times(1)).handleException(
            Thread.currentThread(),
            exception
        );

        verify(logger, times(0)).info(
            "app: {} has no handler for exception:",
            appName,
            exception
        );
    }

    @Test
    public void handleExceptionWithHandlersButException() {
        // given
        EzySimpleApplication app = new EzySimpleApplication();

        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        String appName = RandomUtil.randomShortAlphabetString();
        setting.setName(appName);
        app.setSetting(setting);

        EzyAppHandleExceptionImpl sut = new EzyAppHandleExceptionImpl(app);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        Exception exception = new IllegalArgumentException("one");

        EzyExceptionHandler exceptionHandler = mock(EzyExceptionHandler.class);
        RuntimeException ex = new RuntimeException("just test");
        doThrow(ex).when(exceptionHandler).handleException(
            Thread.currentThread(),
            exception
        );
        app.getExceptionHandlers().addExceptionHandler(exceptionHandler);

        // when
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(exceptionHandler, times(1)).handleException(
            Thread.currentThread(),
            exception
        );

        verify(logger, times(1)).warn(
            "handle exception: {} on app: {} error",
            exceptionToSimpleString(exception),
            appName,
            ex
        );
    }
}

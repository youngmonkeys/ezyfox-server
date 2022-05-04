package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginHandleExceptionImpl;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyPluginHandleExceptionImplTest extends BaseTest {

    @Test
    public void handleExceptionWithEmptyHandlers() {
        // given
        EzySimplePlugin plugin = new EzySimplePlugin();

        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        String pluginName = RandomUtil.randomShortAlphabetString();
        setting.setName(pluginName);
        plugin.setSetting(setting);

        EzyPluginHandleExceptionImpl sut = new EzyPluginHandleExceptionImpl(plugin);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        // when
        Exception exception = new IllegalArgumentException("one");
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(logger, times(1)).info(
            "plugin: {} has no handler for exception:",
            pluginName,
            exception
        );
    }

    @Test
    public void handleExceptionWithHandlers() {
        // given
        EzySimplePlugin plugin = new EzySimplePlugin();

        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        String pluginName = RandomUtil.randomShortAlphabetString();
        setting.setName(pluginName);
        plugin.setSetting(setting);

        EzyPluginHandleExceptionImpl sut = new EzyPluginHandleExceptionImpl(plugin);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        EzyExceptionHandler exceptionHandler = mock(EzyExceptionHandler.class);
        plugin.getExceptionHandlers().addExceptionHandler(exceptionHandler);

        // when
        Exception exception = new IllegalArgumentException("one");
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(exceptionHandler, times(1)).handleException(
            Thread.currentThread(),
            exception
        );

        verify(logger, times(0)).info(
            "plugin: {} has no handler for exception:",
            pluginName,
            exception
        );
    }

    @Test
    public void handleExceptionWithHandlersButException() {
        // given
        EzySimplePlugin plugin = new EzySimplePlugin();

        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        String pluginName = RandomUtil.randomShortAlphabetString();
        setting.setName(pluginName);
        plugin.setSetting(setting);

        EzyPluginHandleExceptionImpl sut = new EzyPluginHandleExceptionImpl(plugin);

        Logger logger = mock(Logger.class);
        FieldUtil.setFieldValue(sut,"logger", logger);

        Exception exception = new IllegalArgumentException("one");

        EzyExceptionHandler exceptionHandler = mock(EzyExceptionHandler.class);
        RuntimeException ex = new RuntimeException("just test");
        doThrow(ex).when(exceptionHandler).handleException(
            Thread.currentThread(),
            exception
        );
        plugin.getExceptionHandlers().addExceptionHandler(exceptionHandler);

        // when
        sut.handle(Thread.currentThread(), exception);

        // then
        verify(exceptionHandler, times(1)).handleException(
            Thread.currentThread(),
            exception
        );

        verify(logger, times(1)).warn(
            "handle exception: {} on plugin: {} error",
            EzyStrings.exceptionToSimpleString(exception),
            pluginName,
            ex
        );
    }
}

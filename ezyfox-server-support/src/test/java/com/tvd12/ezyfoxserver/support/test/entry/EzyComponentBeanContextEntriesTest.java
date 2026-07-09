package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.support.entry.EzyComponentBeanContextEntries;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class EzyComponentBeanContextEntriesTest {

    @Test
    public void nullEventNameTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType(null);

        // then
        Assert.assertNull(actual);
    }

    @Test
    public void eventNameWithoutClassNameTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType("USER_LOGIN");

        // then
        Assert.assertNull(actual);
    }

    @Test
    public void classNotFoundTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType("com.tvd12.ezyfoxserver.support.test.entry.NotExistedClass.FOO");

        // then
        Assert.assertNull(actual);
    }

    @Test
    public void classIsNotEnumTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType("java.lang.String.FOO");

        // then
        Assert.assertNull(actual);
    }

    @Test
    public void enumConstantNotFoundTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType(
                "com.tvd12.ezyfoxserver.support.test.entry.EzyCustomEventType.NOT_EXISTED"
            );

        // then
        Assert.assertNull(actual);
    }

    @Test
    public void enumNotImplementEzyConstantTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType(
                "com.tvd12.ezyfoxserver.support.test.entry.EzyNonConstantEventType.FOO"
            );

        // then
        Assert.assertNull(actual);
    }

    @Test
    public void validCustomEventTypeTest() {
        // when
        EzyConstant actual = EzyComponentBeanContextEntries
            .eventNameToEventType(
                "com.tvd12.ezyfoxserver.support.test.entry.EzyCustomEventType.CUSTOM_EVENT"
            );

        // then
        Assert.assertEquals(actual, EzyCustomEventType.CUSTOM_EVENT);
    }

    @Test
    public void addControllerWithExplicitEventTypeTest() {
        // given
        ExplicitEventTypeController controller = new ExplicitEventTypeController();
        EzySetup setup = mock(EzySetup.class);
        EzyContext context = contextOf(setup);
        EzyBeanContext beanContext = beanContextOf(controller);
        Logger logger = mock(Logger.class);

        // when
        EzyComponentBeanContextEntries.addEventControllersToContext(
            context,
            beanContext,
            logger
        );

        // then
        verify(setup, times(1))
            .addEventController(EzyCustomEventType.CUSTOM_EVENT, controller);
    }

    @Test
    public void addControllerWithBuiltInEventNameTest() {
        // given
        BuiltInEventNameController controller = new BuiltInEventNameController();
        EzySetup setup = mock(EzySetup.class);
        EzyContext context = contextOf(setup);
        EzyBeanContext beanContext = beanContextOf(controller);
        Logger logger = mock(Logger.class);

        // when
        EzyComponentBeanContextEntries.addEventControllersToContext(
            context,
            beanContext,
            logger
        );

        // then
        verify(setup, times(1))
            .addEventController(EzyEventType.USER_LOGIN, controller);
    }

    @Test
    public void addControllerWithCustomClassEventNameTest() {
        // given
        CustomClassEventNameController controller = new CustomClassEventNameController();
        EzySetup setup = mock(EzySetup.class);
        EzyContext context = contextOf(setup);
        EzyBeanContext beanContext = beanContextOf(controller);
        Logger logger = mock(Logger.class);

        // when
        EzyComponentBeanContextEntries.addEventControllersToContext(
            context,
            beanContext,
            logger
        );

        // then
        verify(setup, times(1))
            .addEventController(EzyCustomEventType.CUSTOM_EVENT, controller);
    }

    @Test
    public void skipControllerWithBlankEventNameTest() {
        // given
        BlankEventNameController controller = new BlankEventNameController();
        EzySetup setup = mock(EzySetup.class);
        EzyContext context = contextOf(setup);
        EzyBeanContext beanContext = beanContextOf(controller);
        Logger logger = mock(Logger.class);

        // when
        EzyComponentBeanContextEntries.addEventControllersToContext(
            context,
            beanContext,
            logger
        );

        // then
        verify(setup, never())
            .addEventController(any(), any());
    }

    @Test
    public void skipControllerWithUnknownEventNameTest() {
        // given
        UnknownEventNameController controller = new UnknownEventNameController();
        EzySetup setup = mock(EzySetup.class);
        EzyContext context = contextOf(setup);
        EzyBeanContext beanContext = beanContextOf(controller);
        Logger logger = mock(Logger.class);

        // when
        EzyComponentBeanContextEntries.addEventControllersToContext(
            context,
            beanContext,
            logger
        );

        // then
        verify(setup, never())
            .addEventController(any(), any());
    }

    @Test
    public void addMultipleControllersTest() {
        // given
        ExplicitEventTypeController explicitController = new ExplicitEventTypeController();
        BuiltInEventNameController builtInController = new BuiltInEventNameController();
        CustomClassEventNameController customClassController = new CustomClassEventNameController();
        BlankEventNameController blankController = new BlankEventNameController();
        UnknownEventNameController unknownController = new UnknownEventNameController();
        List<Object> controllers = Arrays.asList(
            explicitController,
            builtInController,
            customClassController,
            blankController,
            unknownController
        );

        EzySetup setup = mock(EzySetup.class);
        EzyContext context = mock(EzyContext.class);
        when(context.get(EzySetup.class)).thenReturn(setup);
        EzyBeanContext beanContext = mock(EzyBeanContext.class);
        when(beanContext.getSingletons(EzyEventHandler.class)).thenReturn(controllers);
        Logger logger = mock(Logger.class);

        // when
        EzyComponentBeanContextEntries.addEventControllersToContext(
            context,
            beanContext,
            logger
        );

        // then
        verify(setup, times(1)).addEventController(
            EzyCustomEventType.CUSTOM_EVENT,
            explicitController
        );
        verify(setup, times(1)).addEventController(
            EzyEventType.USER_LOGIN,
            builtInController
        );
        verify(setup, times(1)).addEventController(
            EzyCustomEventType.CUSTOM_EVENT,
            customClassController
        );
        verify(setup, never()).addEventController(any(), eq(blankController));
        verify(setup, never()).addEventController(any(), eq(unknownController));
        verifyNoMoreInteractions(setup);
    }

    private EzyContext contextOf(EzySetup setup) {
        EzyContext context = mock(EzyContext.class);
        when(context.get(EzySetup.class)).thenReturn(setup);
        return context;
    }

    private EzyBeanContext beanContextOf(Object controller) {
        EzyBeanContext beanContext = mock(EzyBeanContext.class);
        when(beanContext.getSingletons(EzyEventHandler.class))
            .thenReturn(Collections.singletonList(controller));
        return beanContext;
    }
}

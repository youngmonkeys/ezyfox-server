package com.tvd12.ezyfoxserver.support.entry;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfox.core.util.EzyEventHandlerAnnotations;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import org.slf4j.Logger;

import java.util.List;

import static com.tvd12.ezyfox.core.util.EzyEventHandlerLists.sortEventHandlersByPriority;
import static com.tvd12.ezyfox.io.EzyStrings.EMPTY_STRING;
import static com.tvd12.ezyfox.io.EzyStrings.isBlank;

public final class EzyComponentBeanContextEntries {

    private EzyComponentBeanContextEntries() {}

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addEventControllersToContext(
        EzyContext context,
        EzyBeanContext beanContext,
        Logger logger
    ) {
        EzySetup setup = context.get(EzySetup.class);
        List<Object> eventControllers = beanContext
            .getSingletons(EzyEventHandler.class);
        sortEventHandlersByPriority(eventControllers);
        for (Object controller : eventControllers) {
            EzyEventController eventController = (EzyEventController) controller;
            EzyConstant eventType = eventController.getEventType();
            String eventName = EMPTY_STRING;
            if (eventType == null) {
                Class<?> controllerType = controller.getClass();
                EzyEventHandler annotation = controllerType
                    .getAnnotation(EzyEventHandler.class);
                eventName = EzyEventHandlerAnnotations
                    .getEvent(annotation);
                eventType = EzyEventType.of(eventName);
                if (eventType == null) {
                    eventType = eventNameToEventType(eventName);
                }
            }
            if (eventType == null) {
                logger.info(
                    "there is no event with name: {}, skill controller: {}",
                    eventName,
                    controller
                );
            } else {
                logger.info("add event {} controller {}", eventType, controller);
                setup.addEventController(eventType, eventController);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static EzyConstant eventNameToEventType(
        String eventName
    ) {
        try {
            if (isBlank(eventName)) {
                return null;
            }
            int lastDotIndex = eventName.lastIndexOf('.');
            if (lastDotIndex < 0) {
                return null;
            }
            String className = eventName.substring(0, lastDotIndex);
            String constantName = eventName.substring(lastDotIndex + 1);
            Class<?> eventClass = Class.forName(className);
            if (!eventClass.isEnum()) {
                return null;
            }
            Enum eventEnum = Enum.valueOf((Class<Enum>) eventClass, constantName);
            return (EzyConstant) eventEnum;
        } catch (Throwable e) {
            return null;
        }
    }
}

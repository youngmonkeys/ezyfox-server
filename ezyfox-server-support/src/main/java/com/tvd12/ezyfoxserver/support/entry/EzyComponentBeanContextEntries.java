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
            if (eventType == null) {
                Class<?> controllerType = controller.getClass();
                EzyEventHandler annotation = controllerType
                    .getAnnotation(EzyEventHandler.class);
                String eventName = EzyEventHandlerAnnotations
                    .getEvent(annotation);
                eventType = EzyEventType.valueOf(eventName);
            }
            setup.addEventController(eventType, eventController);
            logger.info("add event {} controller {}", eventType, controller);
        }
    }
}

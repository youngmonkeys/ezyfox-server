package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.setting.EzyEventControllerSetting;
import com.tvd12.ezyfoxserver.setting.EzyEventControllersSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EzyEventControllersImpl implements EzyEventControllers {

    @SuppressWarnings("rawtypes")
    protected final Map<EzyConstant, List<EzyEventController>> controllers
        = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    public static EzyEventControllers create(EzyEventControllersSetting setting) {
        EzyEventControllers controllers = new EzyEventControllersImpl();
        for (EzyEventControllerSetting item : setting.getEventControllers()) {
            EzyEventType eventType = EzyEventType.valueOf(item.getEventType());
            EzyEventController controller = EzyClasses.newInstance(item.getController());
            controllers.addController(eventType, controller);
        }
        return controllers;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addController(EzyConstant eventType, EzyEventController controller) {
        controllers.compute(eventType, (k, v) -> {
            List<EzyEventController> list = v != null
                ? new ArrayList<>(v)
                : new ArrayList<>();
            list.add(controller);
            return list;
        });
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<EzyEventController> getControllers(EzyConstant eventType) {
        return controllers.getOrDefault(eventType, Collections.emptyList());
    }

    @Override
    public void destroy() {
        controllers.clear();
    }
}

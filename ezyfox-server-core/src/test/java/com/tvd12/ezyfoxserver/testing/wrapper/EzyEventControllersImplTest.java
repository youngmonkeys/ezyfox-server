package com.tvd12.ezyfoxserver.testing.wrapper;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllerSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.test.base.BaseTest;

@SuppressWarnings("rawtypes")
public class EzyEventControllersImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleEventControllersSetting controllersSetting = new EzySimpleEventControllersSetting();
        EzySimpleEventControllerSetting setting1 = new EzySimpleEventControllerSetting();
        setting1.setEventType(EzyEventType.SERVER_READY.toString());
        setting1.setController(EventController1.class.getName());
        controllersSetting.setItem(setting1);
        EzyEventControllers controllers = EzyEventControllersImpl.create(controllersSetting);
        controllers.addController(EzyEventType.SERVER_INITIALIZING, new EventController2());
        controllers.destroy();
    }
    
    public static class EventController1 implements EzyEventController {

        @Override
        public void handle(Object ctx, Object event) {
        }
        
    }
    
    public static class EventController2 implements EzyEventController {

        @Override
        public void handle(Object ctx, Object event) {
        }
        
    }

}

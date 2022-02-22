package com.tvd12.ezyfoxserver.testing.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.util.EzyThreads;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllerSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

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
    
    @SuppressWarnings("unchecked")
    @Test
    public void multiThreadTest() {
        EzyEventControllersImpl sut = new EzyEventControllersImpl();
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        AtomicBoolean active = new AtomicBoolean(true);
        executorService.execute(() -> {
            while (active.get()) {
                for (EzyEventType eventType : EzyEventType.values()) {
                    sut.addController(eventType, mock(EzyEventController.class));
                }
                EzyThreads.sleep(1);
            }
        });
        executorService.execute(() -> {
            while (active.get()) {
                for (EzyEventType eventType : EzyEventType.values()) {
                    for(EzyEventController controller : sut.getControllers(eventType)) {
                        controller.handle(null, null);
                    }
                }
                EzyThreads.sleep(1);
            }
        });
        EzyThreads.sleep(1000);
        executorService.shutdown();
    }
    
    @Test
    public void getListControllerTest() {
        // given
        EzyEventControllersImpl sut = new EzyEventControllersImpl();
        EzyEventController c1 = mock(EzyEventController.class);
        EzyEventController c2 = mock(EzyEventController.class);
        sut.addController(EzyEventType.SERVER_INITIALIZING, c1);
        sut.addController(EzyEventType.SERVER_INITIALIZING, c2);
        
        // when
        List<EzyEventController> controllers = sut.getControllers(EzyEventType.SERVER_INITIALIZING);
        
        // then
        Asserts.assertEquals(controllers, Arrays.asList(c1, c2), false);
        Asserts.assertEmpty(sut.getControllers(EzyEventType.USER_ACCESS_APP));
        
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

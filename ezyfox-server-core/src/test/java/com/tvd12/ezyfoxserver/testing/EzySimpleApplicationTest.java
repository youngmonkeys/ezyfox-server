package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzySimpleApplicationTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        app.setSetting(setting);
        assert app.equals(app);
        assert !app.equals(new EzySimpleApplication());

        EzyAppRequestController controller = mock(EzyAppRequestController.class);
        app.setRequestController(controller);
        assert app.getRequestController() == controller;
        app.destroy();
        app.destroy();
    }

}

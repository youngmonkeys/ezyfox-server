package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyServerControllersImplTest extends BaseTest {

    @Test
    public void test() {
        EzyServerControllers controller = EzyServerControllersImpl.builder().build();
        assert controller.getController(EzyCommand.APP_ACCESS) != null;
        assert controller.getInterceptor(EzyCommand.LOGIN) != null;
        assert controller.getStreamingController() != null;
        assert controller.getStreamingInterceptor() != null;
    }

}

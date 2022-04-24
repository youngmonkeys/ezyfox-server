package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyMessageControllerTest extends BaseCoreTest {

    @Test
    public void test() {
        MyEzyMessageController controller = new MyEzyMessageController();
        controller.newArrayBuilder();
    }

    public class MyEzyMessageController extends EzyMessageController {
        @Override
        public EzyArrayBuilder newArrayBuilder() {
            return super.newArrayBuilder();
        }
    }

}

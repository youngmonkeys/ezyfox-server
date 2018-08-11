package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

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

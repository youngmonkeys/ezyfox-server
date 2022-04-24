package com.tvd12.ezyfoxserver.testing.constant;

import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyMaxRequestPerSecondActionTest extends BaseTest {

    @Test
    public void test() {
        assert EzyMaxRequestPerSecondAction.DROP_REQUEST.getId() == 1;
        System.out.print(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION.getName());
    }
}

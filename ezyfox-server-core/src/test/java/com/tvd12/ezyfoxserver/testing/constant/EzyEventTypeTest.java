package com.tvd12.ezyfoxserver.testing.constant;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyEventTypeTest extends BaseTest {

    @Test
    public void test() {
        assert EzyEventType.USER_LOGIN.getId() >= 0;
        assert EzyEventType.USER_LOGIN.getName().equals("USER_LOGIN");
        EzyEventType.valueOf("USER_LOGIN");
    }

    @Test()
    public void test1() {}
}

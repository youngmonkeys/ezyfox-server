package com.tvd12.ezyfoxserver.testing.constant;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.test.base.BaseTest;

public class EzyLoginErrorTest extends BaseTest {

    @Test
    public void test() {
        EzyLoginError value = EzyLoginError.ALREADY_LOGIN;
        assert value.getId() == 1;
        assert value.getName().equals("ALREADY_LOGIN");
        EzyLoginError.valueOf("ALREADY_LOGIN");
    }
    
}

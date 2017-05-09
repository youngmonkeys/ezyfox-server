package com.tvd12.ezyfoxserver.testing.constant;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.test.base.BaseTest;

public class EzyEventTypeTest extends BaseTest {

    @Test
    public void test() {
        assert EzyEventType.USER_LOGIN.getId() >= 0;
        assert EzyEventType.USER_LOGIN.getName().equals("USER_LOGIN");
        assert EzyEventType.valueOf(EzyEventType.USER_LOGIN.getId()) == EzyEventType.USER_LOGIN;
        EzyEventType.values();
        EzyEventType.valueOf("USER_LOGIN");
    }
    
    @Test()
    public void test1() {
        
    }
    
}

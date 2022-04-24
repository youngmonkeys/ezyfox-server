package com.tvd12.ezyfoxserver.testing.constant;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyConnectionTypeTest extends BaseTest {

    @Test
    public void test() {
        assert EzyConnectionType.SOCKET.getId() == 1;
        System.out.println(EzyConnectionType.WEBSOCKET.getName());
    }

}

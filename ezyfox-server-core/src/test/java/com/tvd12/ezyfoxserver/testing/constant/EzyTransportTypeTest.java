package com.tvd12.ezyfoxserver.testing.constant;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyTransportTypeTest extends BaseTest {

    @Test
    public void test() {
        assert EzyTransportType.TCP.getId() == 1;
        System.out.println(EzyTransportType.UDP.getName());
    }

}

package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.socket.EzySimpleBytesPackage;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleBytesPackageTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleBytesPackage pack = new EzySimpleBytesPackage();
        pack.setTransportType(EzyTransportType.TCP);
        assert pack.getTransportType() == EzyTransportType.TCP;
    }

}

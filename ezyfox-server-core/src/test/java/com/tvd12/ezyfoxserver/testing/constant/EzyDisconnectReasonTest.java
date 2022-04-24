package com.tvd12.ezyfoxserver.testing.constant;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyDisconnectReasonTest extends BaseTest {

    @Test
    public void test() {
        EzyDisconnectReason value = EzyDisconnectReason.IDLE;
        assert value.getId() == 1;
        assert value == EzyDisconnectReason.valueOf(1);
        assert value.getName().equals("IDLE");
        EzyDisconnectReason.valueOf("IDLE");
    }
}

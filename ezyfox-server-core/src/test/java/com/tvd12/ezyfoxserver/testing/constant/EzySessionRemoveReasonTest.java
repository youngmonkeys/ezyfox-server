package com.tvd12.ezyfoxserver.testing.constant;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
import com.tvd12.test.base.BaseTest;

public class EzySessionRemoveReasonTest extends BaseTest {

    @Test
    public void test() {
        EzySessionRemoveReason reason = EzySessionRemoveReason.UNKNOWN;
        assert reason.getId() == 0;
        assert reason == EzySessionRemoveReason.valueOf(0);
        assert reason.getName().equals("UNKNOWN");
        EzySessionRemoveReason.valueOf("UNKNOWN");
    }
    
}

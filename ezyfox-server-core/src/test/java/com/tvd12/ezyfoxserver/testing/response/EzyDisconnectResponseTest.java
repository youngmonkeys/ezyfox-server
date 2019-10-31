package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.response.EzyDisconnectParams;
import com.tvd12.ezyfoxserver.response.EzyDisconnectResponse;
import com.tvd12.test.base.BaseTest;

public class EzyDisconnectResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyDisconnectParams params = new EzyDisconnectParams();
        params.setReason(EzyDisconnectReason.ADMIN_BAN);
        assert params.getReason() == EzyDisconnectReason.ADMIN_BAN;
        EzyDisconnectResponse response = new EzyDisconnectResponse(params);
        response.serialize();
        response.release();
    }
    
}

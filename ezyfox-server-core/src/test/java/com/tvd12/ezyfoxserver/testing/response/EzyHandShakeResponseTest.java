package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.response.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;
import com.tvd12.test.base.BaseTest;

public class EzyHandShakeResponseTest extends BaseTest {
    
    @Test
    public void test() {
        EzyHandShakeParams params = new EzyHandShakeParams();
        params.setToken("123");
        params.setClientKey(new byte[0]);
        params.setServerPublicKey(new byte[0]);
        assert params.getToken().equals("123");
        assert params.getClientKey().length == 0;
        assert params.getServerPublicKey().length == 0;
        EzyHandShakeResponse response = new EzyHandShakeResponse(params);
        response.serialize();
        params.setServerPublicKey(null);
        response.serialize();
        response.release();
    }

}

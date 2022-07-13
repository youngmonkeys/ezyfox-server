package com.tvd12.ezyfoxserver.testing.response;

import com.tvd12.ezyfox.security.EzyAesCrypt;
import com.tvd12.ezyfoxserver.response.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyHandShakeResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyHandShakeParams params = new EzyHandShakeParams();
        params.setReconnectToken("123");
        params.setServerPublicKey(new byte[0]);
        params.setSessionId(1L);
        params.setSessionKey(EzyAesCrypt.randomKey());
        assert params.getSessionId() == 1L;
        assert params.getReconnectToken().equals("123");
        assert params.getServerPublicKey().length == 0;
        assert params.getSessionKey() != null;
        EzyHandShakeResponse response = new EzyHandShakeResponse(params);
        response.serialize();
        params.setServerPublicKey(null);
        response.serialize();
        response.release();
    }
}

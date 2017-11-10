package com.tvd12.ezyfoxserver.testing.request;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.request.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleHandShakeParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleHandShakeRequestTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyHandShakeParams request = EzySimpleHandShakeParams.builder()
                .clientId("abc")
                .clientKey("def")
                .reconnectToken("haha")
                .build();
        request.toString();
        
    }
    
}

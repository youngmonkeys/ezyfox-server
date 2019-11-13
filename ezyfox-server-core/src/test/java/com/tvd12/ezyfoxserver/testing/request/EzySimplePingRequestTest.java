package com.tvd12.ezyfoxserver.testing.request;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.request.EzySimplePingRequest;

public class EzySimplePingRequestTest {

    @Test
    public void test() {
        EzySimplePingRequest rq = new EzySimplePingRequest();
        rq.deserializeParams(null);
        rq.release();
    }
    
}

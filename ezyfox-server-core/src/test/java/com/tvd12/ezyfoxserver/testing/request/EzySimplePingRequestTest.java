package com.tvd12.ezyfoxserver.testing.request;

import com.tvd12.ezyfoxserver.request.EzySimplePingRequest;
import org.testng.annotations.Test;

public class EzySimplePingRequestTest {

    @Test
    public void test() {
        EzySimplePingRequest rq = new EzySimplePingRequest();
        rq.deserializeParams(null);
        rq.release();
    }

}

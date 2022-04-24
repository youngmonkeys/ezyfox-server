package com.tvd12.ezyfoxserver.testing.request;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.request.EzySimpleAccessAppRequest;
import org.testng.annotations.Test;

public class EzySimpleAccessAppParamsTest {

    @Test
    public void test() {
        EzySimpleAccessAppRequest request = new EzySimpleAccessAppRequest();
        request.deserializeParams(EzyEntityFactory.newArrayBuilder()
            .append("test")
            .append(EzyEntityFactory.newObjectBuilder())
            .build());
        assert request.getParams().getData() != null;
        request.release();
    }
}

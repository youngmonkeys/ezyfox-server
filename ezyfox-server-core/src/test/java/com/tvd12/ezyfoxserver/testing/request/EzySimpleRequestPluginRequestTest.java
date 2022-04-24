package com.tvd12.ezyfoxserver.testing.request;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzySimpleRequestPluginRequestTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleRequestAppRequest request = new EzySimpleRequestAppRequest();
        request.deserializeParams(EzyEntityFactory.newArrayBuilder()
            .append(1)
            .append(EzyEntityFactory.newArrayBuilder())
            .build());
        request.release();
    }
}

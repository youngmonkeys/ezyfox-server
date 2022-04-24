package com.tvd12.ezyfoxserver.testing.request;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestPluginRequest;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzySimpleRequestAppRequestTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleRequestPluginRequest request = new EzySimpleRequestPluginRequest();
        request.deserializeParams(EzyEntityFactory.newArrayBuilder()
            .append(1)
            .append(EzyEntityFactory.newArrayBuilder())
            .build());
        request.release();
    }

}

package com.tvd12.ezyfoxserver.testing.request;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestParams;
import org.testng.annotations.Test;

public class EzySimpleRequestParamsTest {

    @Test
    public void test() {
        EzySimpleRequestParams params = new EzySimpleRequestParams();
        params.deserialize(EzyEntityFactory.newArray());
    }
}

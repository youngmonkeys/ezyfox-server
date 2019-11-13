package com.tvd12.ezyfoxserver.testing.request;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestParams;

public class EzySimpleRequestParamsTest {

    @Test
    public void test() {
        EzySimpleRequestParams params = new EzySimpleRequestParams();
        params.deserialize(EzyEntityFactory.newArray());
    }
    
}

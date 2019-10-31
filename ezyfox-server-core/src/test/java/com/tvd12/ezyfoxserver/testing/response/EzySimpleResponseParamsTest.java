package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.response.EzySimpleResponseParams;
import com.tvd12.test.base.BaseTest;

public class EzySimpleResponseParamsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleResponseParams params = new EzySimpleResponseParams();
        params.serialize();
        params.release();
    }
    
}

package com.tvd12.ezyfoxserver.testing.response;

import com.tvd12.ezyfoxserver.response.EzySimpleResponseParams;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleResponseParamsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleResponseParams params = new EzySimpleResponseParams();
        params.serialize();
        params.release();
    }

}

package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyAccessAppError;
import com.tvd12.ezyfoxserver.response.EzyAccessAppErrorResponse;
import com.tvd12.ezyfoxserver.response.EzyErrorParams;
import com.tvd12.test.base.BaseTest;

public class EzyAccessAppErrorResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyErrorParams params = new EzyErrorParams();
        params.setCode(1);
        params.setMessage("m");
        params.setError(EzyAccessAppError.MAXIMUM_USER);
        assert params.getCode() == EzyAccessAppError.MAXIMUM_USER.getId();
        assert params.getMessage().equals(EzyAccessAppError.MAXIMUM_USER.getMessage());
        EzyAccessAppErrorResponse response = new EzyAccessAppErrorResponse(params);
        response.release();
    }
    
}

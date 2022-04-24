package com.tvd12.ezyfoxserver.testing.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyRequestAppError;
import com.tvd12.ezyfoxserver.response.EzyErrorParams;
import com.tvd12.ezyfoxserver.response.EzyRequestAppErrorResponse;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyRequestAppErrorResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyErrorParams params = new EzyErrorParams();
        params.setError(EzyRequestAppError.HAS_NOT_ACCESSED);
        EzyRequestAppErrorResponse response = new EzyRequestAppErrorResponse(params);
        assert response.getCommand() == EzyCommand.APP_REQUEST_ERROR;
        assert response.getParams() == params;
        response.release();
    }

}

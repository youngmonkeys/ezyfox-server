package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponseParams;
import com.tvd12.test.base.BaseTest;

public class EzyRequestAppResponseTest extends BaseTest {
    
    @Test
    public void test() {
        EzyRequestAppResponseParams params = new EzyRequestAppResponseParams();
        EzyData data = EzyEntityFactory.EMPTY_ARRAY;
        params.setData(data);
        params.setAppId(1);
        assert params.getAppId() == 1;
        assert params.getData() == data;
        EzyRequestAppResponse response = new EzyRequestAppResponse(params);
        assert response.getParams() == params;
        assert response.getCommand() == EzyCommand.APP_REQUEST;
        assert response.serialize().size() > 0;
        response.release();
    }

}

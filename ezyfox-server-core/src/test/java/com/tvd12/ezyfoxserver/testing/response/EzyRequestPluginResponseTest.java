package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponseParams;
import com.tvd12.test.base.BaseTest;

public class EzyRequestPluginResponseTest extends BaseTest {
    
    @Test
    public void test() {
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        EzyRequestPluginResponseParams params = new EzyRequestPluginResponseParams();
        params.setData(data);
        params.setPluginId(1);
        assert params.getData() == data;
        assert params.getPluginId() == 1;
        EzyRequestPluginResponse response = new EzyRequestPluginResponse(params);
        assert response.getParams() == params;
        assert response.serialize().size() >= 0;
        response.release();
    }

}

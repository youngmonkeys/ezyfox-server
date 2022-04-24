package com.tvd12.ezyfoxserver.testing.response;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.response.EzyLoginParams;
import com.tvd12.ezyfoxserver.response.EzyLoginResponse;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyLoginResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        EzyLoginParams params = new EzyLoginParams();
        params.setZoneId(1);
        params.setZoneName("test");
        params.setUserId(2);
        params.setData(data);
        params.setUsername("name");
        assert params.getZoneId() == 1;
        assert params.getZoneName().equals("test");
        assert params.getUserId() == 2;
        assert params.getData() == data;
        assert params.getUsername().equals("name");
        EzyLoginResponse response = new EzyLoginResponse(params);
        assert response.getParams() == params;
        response.release();
    }

}

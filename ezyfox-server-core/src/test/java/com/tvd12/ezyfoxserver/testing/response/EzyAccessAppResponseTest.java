package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.response.EzyAccessAppParams;
import com.tvd12.ezyfoxserver.response.EzyAccessAppResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;

public class EzyAccessAppResponseTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        EzyAccessAppParams params = new EzyAccessAppParams();
        EzyData data = EzyEntityFactory.EMPTY_ARRAY;
        params.setData(data);
        params.setApp(setting);
        assert params.getApp() == setting;
        assert params.getData() == data;
        EzyAccessAppResponse response = new EzyAccessAppResponse(params);
        assert response.getParams() == params;
        assert response.getCommand() == EzyCommand.APP_ACCESS;
        assert response.serialize().size() > 0;
        response.release();
    }

}

package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.response.EzyExitedAppParams;
import com.tvd12.ezyfoxserver.response.EzyExitedAppResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;

public class EzyExitedAppResponseTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        EzyExitedAppParams params = new EzyExitedAppParams();
        params.setReason(EzyUserRemoveReason.EXIT_APP);
        params.setApp(setting);
        EzyExitedAppResponse response = new EzyExitedAppResponse(params);
        assert response.getParams() == params;
        assert response.getCommand() == EzyCommand.APP_EXIT;
        assert response.serialize().size() > 0;
        assert params.getApp() == setting;
        assert params.getReason() == EzyUserRemoveReason.EXIT_APP;
        response.release();
    }

}

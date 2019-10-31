package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.response.EzyPluginInfoParams;
import com.tvd12.ezyfoxserver.response.EzyPluginInfoResponse;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;

public class EzyPluginInfoReponseTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimplePluginSetting setting = new EzySimplePluginSetting(); 
        setting.setName("test");
        EzyPluginInfoParams params = new EzyPluginInfoParams();
        params.setPlugin(setting);
        assert params.getPlugin() == setting;
        EzyPluginInfoResponse response = new EzyPluginInfoResponse(params);
        assert response.getParams() == params;
        response.serialize();
        response.release();
    }

}

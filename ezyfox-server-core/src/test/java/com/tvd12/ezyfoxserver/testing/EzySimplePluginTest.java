package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzySimplePluginTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        plugin.setSetting(setting);
        assert plugin.equals(plugin);
        assert !plugin.equals(new EzySimplePlugin());
        
        EzyPluginRequestController controller = mock(EzyPluginRequestController.class);
        plugin.setRequestController(controller);
        assert plugin.getRequestController() == controller;
        plugin.destroy();
        plugin.destroy();
    }
    
}

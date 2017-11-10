package com.tvd12.ezyfoxserver.testing;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;

public class EzyLoaderTest extends BaseCoreTest {
    
    @Test
    public void test() throws Exception {
        EzySimpleServer server = newServer();
        
        EzyConfig config = server.getConfig();
        assertEquals(config.getEzyfoxHome(), "test-data");
        assertEquals(config.getEzyfoxVersion(), "1.0.0");
        assertEquals(config.getLoggerConfigFile(), "logback.groovy");
        
        EzySettings settings = server.getSettings();
        EzyAppSetting app = settings.getAppByName("ezyfox-chat");
        app = settings.getAppById(app.getId());
        assertEquals(app.getName(), "ezyfox-chat");
        assertEquals(app.getId() > 0, true);
        assertEquals(app.getWorkerPoolSize() > 0, true);
        assertEquals(app.getEntryLoader(), "com.tvd12.ezyfoxserver.chat.EzyChatEntryLoader");
        assertEquals(app.getEventControllers().getController(EzyEventType.SERVER_READY) != null, true);
        
        EzyPluginSetting plugin = settings.getPluginByName("ezyfox-auth-plugin");
        plugin = settings.getPluginById(plugin.getId());
        assertEquals(plugin.getId() > 0, true);
        assertEquals(plugin.getWorkerPoolSize() > 0, true);
        assertEquals(plugin.getPriority(), -1);
        assertEquals(plugin.getName(), "ezyfox-auth-plugin");
        assertEquals(plugin.getEventControllers().getController(EzyEventType.SERVER_READY) != null, true);
        
//        Class<EzyAppEntryLoader> chatEntryLoaderClass = server.getAppEntryLoaderClass("ezyfox-chat");
//        assertEquals(chatEntryLoaderClass.getName(), "com.tvd12.ezyfoxserver.mapping.chat.EzyChatEntryLoader");
//        Class<EzyPluginEntryLoader> authEntryLoaderClass = server.getPluginEntryLoaderClass("ezyfox-auth-plugin");
//        assertEquals(authEntryLoaderClass.getName(), "com.tvd12.ezyfoxserver.mapping.plugin.auth.EzyAuthPluginEntryLoader");
    }

    
}

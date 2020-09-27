package com.tvd12.ezyfoxserver.testing.setting;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractZoneEventController;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting.EzySimpleListenEvents;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;

public class EzyZoneSettingBuilderTest {

    @Test
    public void test() {
        EzySimpleAppSetting appSetting = new EzyAppSettingBuilder()
                .configFile("config.properties")
                .entryLoader(TestAppEntryLoader.class)
                .entryLoaderArgs(new String[] {"hello"})
                .maxUsers(100)
                .name("test")
                .threadPoolSize(3)
                .build();
        EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
        EzySimpleListenEvents listenEvents = new EzySimpleListenEvents();
        EzySimplePluginSetting pluginSetting = new EzyPluginSettingBuilder()
                .configFile("config.properties")
                .entryLoader(TestPluginEntryLoader.class)
                .name("test")
                .threadPoolSize(3)
                .priority(1)
                .listenEvents(listenEvents)
                .addListenEvent(EzyEventType.USER_LOGIN)
                .addListenEvent(EzyEventType.USER_LOGIN.toString())
                .build();
        EzySimplePluginsSetting pluginsSetting = new EzySimplePluginsSetting();
        EzySimpleStreamingSetting streamingSetting = new EzySimpleStreamingSetting();
        EzySimpleEventControllersSetting eventControllersSetting = new EzySimpleEventControllersSetting();
        EzySimpleUserManagementSetting userManagementSetting = new EzyUserManagementSettingBuilder()
                .allowChangeSession(true)
                .allowGuestLogin(true)
                .guestNamePrefix("Guest#")
                .maxSessionPerUser(3)
                .userMaxIdleTimeInSecond(100)
                .userNamePattern("user#name")
                .build();
        EzySimpleZoneSetting setting = new EzyZoneSettingBuilder()
                .configFile("config.properties")
                .maxUsers(1000)
                .name("test")
                .applications(appsSetting)
                .application(appSetting)
                .plugins(pluginsSetting)
                .plugin(pluginSetting)
                .streaming(streamingSetting)
                .eventControllers(eventControllersSetting)
                .userManagement(userManagementSetting)
                .addEventController(EzyEventType.SERVER_READY, HelloZoneServerReadyController.class)
                .build();
        assertEquals(setting.getConfigFile(), "config.properties");
        assertEquals(setting.getMaxUsers(), 1000);
        assertEquals(setting.getName(), "test");
        assertEquals(setting.getApplications(), appsSetting);
        assertEquals(setting.getPlugins(), pluginsSetting);
        assertEquals(setting.getStreaming(), streamingSetting);
        assertEquals(setting.getEventControllers(), eventControllersSetting);
        assertEquals(setting.getUserManagement(), userManagementSetting);
        
        appSetting = appsSetting.getAppByName("test");
        assertEquals(appSetting.getConfigFile(true), "config.properties");
        assertEquals(appSetting.getEntryLoader(), TestAppEntryLoader.class.getName());
        assertEquals(appSetting.getFolder(), "test");
        assertEquals(appSetting.getMaxUsers(), 100);
        assertEquals(appSetting.getName(), "test");
        assertEquals(appSetting.getThreadPoolSize(), 3);
        assertEquals(appSetting.getConfigFileInput(), "config.properties");
        
        pluginSetting = pluginsSetting.getPluginByName("test");
        assertEquals(pluginSetting.getConfigFile(true), "config.properties");
        assertEquals(pluginSetting.getEntryLoader(), TestPluginEntryLoader.class.getName());
        assertEquals(pluginSetting.getFolder(), "test");
        assertEquals(pluginSetting.getName(), "test");
        assertEquals(pluginSetting.getThreadPoolSize(), 3);
        assertEquals(pluginSetting.getPriority(), 1);
        assertEquals(pluginSetting.getListenEvents().getEvents().size(), 1);
        
        userManagementSetting = setting.getUserManagement();
        assertEquals(userManagementSetting.isAllowChangeSession(), true);
        assertEquals(userManagementSetting.isAllowGuestLogin(), true);
        assertEquals(userManagementSetting.getGuestNamePrefix(), "Guest#");
        assertEquals(userManagementSetting.getMaxSessionPerUser(), 3);
        assertEquals(userManagementSetting.getUserMaxIdleTimeInSecond(), 100);
        assertEquals(userManagementSetting.getUserNamePattern(), "user#name");
    }
    
    public static class TestAppEntryLoader implements EzyAppEntryLoader {
        
        public TestAppEntryLoader(String config) {
            
        }

        @Override
        public EzyAppEntry load() throws Exception {
            return null;
        }
        
    }
    
    public static class TestPluginEntryLoader implements EzyPluginEntryLoader {

        @Override
        public EzyPluginEntry load() throws Exception {
            return null;
        }
        
    }
    
    public static class HelloZoneServerReadyController
            extends EzyAbstractZoneEventController<EzyServerReadyEvent> {
        
        @Override
        public void handle(EzyZoneContext ctx, EzyServerReadyEvent event) {
            // add logic here
        }

    }
}

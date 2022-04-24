package com.tvd12.ezyfoxserver.embedded.test;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleUdpSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUdpSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;

public class EzyEmbeddedServerTest {

    public static void main(String[] args) throws Exception {
        new EzyEmbeddedServerTest().test();
    }

    @Test
    public void test() throws Exception {
        EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
                .name("test")
                .entryLoader(TestPluginEntryLoader.class);

        EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
                .name("test")
                .entryLoader(TestAppEntryLoader.class);

        EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
                .name("test")
                .application(appSettingBuilder.build())
                .plugin(pluginSettingBuilder.build());

        EzySimpleUdpSetting udpSetting = new EzyUdpSettingBuilder()
                .active(true)
                .build();

        EzySimpleSettings settings = new EzySettingsBuilder()
                .zone(zoneSettingBuilder.build())
                .udp(udpSetting)
                .build();

        EzyEmbeddedServer server = EzyEmbeddedServer.builder()
                .settings(settings)
                .config(EzySimpleConfig.defaultConfig())
                .configFile("test-config/config.properties")
                .build();
        server.start();
        Thread.sleep(2000);
        server.stop();
    }

    @Test
    public void startWithConfigFile() throws Exception {
        EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
                .name("test")
                .entryLoader(TestPluginEntryLoader.class);

        EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
                .name("test")
                .entryLoader(TestAppEntryLoader.class);

        EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
                .name("test")
                .application(appSettingBuilder.build())
                .plugin(pluginSettingBuilder.build());

        EzySimpleSocketSetting socketSetting = new EzySocketSettingBuilder()
                .active(false) // active or not,  default true
                .build();

        EzySimpleWebSocketSetting webSocketSetting = new EzyWebSocketSettingBuilder()
                .active(false) // active or not,  default true
                .build();

        EzySimpleSettings settings = new EzySettingsBuilder()
                .socket(socketSetting)
                .websocket(webSocketSetting)
                .zone(zoneSettingBuilder.build())
                .build();

        EzyEmbeddedServer server = EzyEmbeddedServer.builder()
                .settings(settings)
                .configFile("test-config/config.properties")
                .build();
        server.start();
        Thread.sleep(1000);
        server.stop();
    }

    @Test
    public void stopTest() {
        EzySimpleSettings settings = new EzySettingsBuilder()
                .build();
        EzyEmbeddedServer server = EzyEmbeddedServer.builder()
                .settings(settings)
                .build();
        server.stop();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void settingNullTest() {
        EzyEmbeddedServer.builder().build();
    }

}

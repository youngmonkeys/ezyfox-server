package com.tvd12.ezyfoxserver.embedded.test;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;

public class EzyEmbeddedServerTest {

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
		
		EzySimpleSettings settings = new EzySettingsBuilder()
				.zone(zoneSettingBuilder.build())
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
	
}

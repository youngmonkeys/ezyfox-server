package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;

public class EzyEmbeddedServerTest {

	public static void main(String[] args) throws Exception {
		EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
		pluginSetting.setName("test");
		pluginSetting.setEntryLoader(TestPluginEntryLoader.class);
		
		EzySimplePluginsSetting pluginsSetting = new EzySimplePluginsSetting();
		pluginsSetting.setItem(pluginSetting);
		
		EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
		appSetting.setName("test");
		appSetting.setEntryLoader(TestAppEntryLoader.class);
		
		EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
		appsSetting.setItem(appSetting);
		
		EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
		zoneSetting.setName("test");
		zoneSetting.setPlugins(pluginsSetting);
		zoneSetting.setApplications(appsSetting);
		
		EzySimpleSettings settings = new EzySimpleSettings();
		settings.addZone(zoneSetting);
		EzyEmbeddedServer server = EzyEmbeddedServer.builder()
				.settings(settings)
				.build();
		server.start();
	}
	
}

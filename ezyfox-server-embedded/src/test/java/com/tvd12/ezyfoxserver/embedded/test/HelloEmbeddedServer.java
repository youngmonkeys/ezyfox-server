package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.ext.EzyAbstractAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestAppSingletonController;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPluginSingletonController;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;

public class HelloEmbeddedServer {

	public static void main(String[] args) throws Exception {
		EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
				.name("hello")
				.addListenEvent(EzyEventType.USER_LOGIN)
				.entryLoader(HelloPluginEntryLoader.class);
		
		EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
				.name("hello")
				.entryLoader(HelloAppEntryLoader.class);
		
		EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
				.name("hello")
				.application(appSettingBuilder.build())
				.plugin(pluginSettingBuilder.build());
		
		EzyWebSocketSettingBuilder webSocketSettingBuilder = new EzyWebSocketSettingBuilder()
				.active(false);
		
		EzySimpleSettings settings = new EzySettingsBuilder()
				.zone(zoneSettingBuilder.build())
				.websocket(webSocketSettingBuilder.build())
				.build();
		
		EzyEmbeddedServer server = EzyEmbeddedServer.builder()
				.settings(settings)
				.build();
		server.start();
	}
	
	public static class HelloAppEntry extends EzySimpleAppEntry {

		@Override
		protected String[] getScanableBeanPackages() {
			return new String[] {
					"com.tvd12.ezyfoxserver.embedded.test" // replace by your package
			};
		}

		@Override
		protected String[] getScanableBindingPackages() {
			return new String[] {
					"com.tvd12.ezyfoxserver.embedded.test" // replace by your package
			};
		}
		
		@Override
		protected EzyAppRequestController newUserRequestController(EzyBeanContext beanContext) {
			return EzyUserRequestAppSingletonController.builder()
					.beanContext(beanContext)
					.build();
		}
		
	}
	
	public static class HelloAppEntryLoader extends EzyAbstractAppEntryLoader {

		@Override
		public EzyAppEntry load() throws Exception {
			return new HelloAppEntry();
		}
		
	}
	
	public static class HelloPluginEntry extends EzySimplePluginEntry {

		@Override
		protected String[] getScanableBeanPackages() {
			return new String[] {
					"com.tvd12.ezyfoxserver.embedded.test.plugin" // replace by your package
			};
		}
		
		@Override
		protected EzyPluginRequestController newUserRequestController(EzyBeanContext beanContext) {
			return EzyUserRequestPluginSingletonController.builder()
					.beanContext(beanContext)
					.build();
		}

	}

	public static class HelloPluginEntryLoader extends EzyAbstractPluginEntryLoader {

		@Override
		public EzyPluginEntry load() throws Exception {
			return new HelloPluginEntry();
		}
		
	}
	
}

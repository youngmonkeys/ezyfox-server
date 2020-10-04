package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.ext.EzyAbstractAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;

public class HelloEmbeddedServer2 {

	public static void main(String[] args) throws Exception {
		EzySimpleSettings settings = new EzyEmbeddedSettingsBuilderPrototype()
				.scan("com.tvd12.ezyfoxserver.embedded.test")
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

	}

	public class HelloPluginEntryLoader extends EzyAbstractPluginEntryLoader {

		@Override
		public EzyPluginEntry load() throws Exception {
			return new HelloPluginEntry();
		}
		
	}
	
}

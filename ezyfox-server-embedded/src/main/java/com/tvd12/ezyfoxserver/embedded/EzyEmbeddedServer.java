package com.tvd12.ezyfoxserver.embedded;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyStoppable;
import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzySettings;

public class EzyEmbeddedServer implements EzyStoppable {

	protected final EzyConfig config;
	protected final String configFile;
	protected final EzySettings settings;
	protected EzyServerContext serverContext;
	
	protected EzyEmbeddedServer(Builder builder) {
		this.config = builder.config;
		this.settings = builder.settings;
		this.configFile = builder.configFile;
	}
	
	public EzyServerContext start() throws Exception {
		EzyRunner runner = EzyEmbeddedRunner.builder()
				.config(config)
				.settings(settings)
				.build();
		runner.run(new String[] {configFile});
		serverContext = runner.getServerContext();
		return serverContext;
	}
	
	@Override
	public void stop() {
		if(serverContext != null)
			((EzyDestroyable)serverContext).destroy();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyEmbeddedServer> {
		
		protected EzyConfig config;
		protected String configFile;
		protected EzySettings settings;
		
		public Builder config(EzyConfig config) {
			this.config = config;
			return this;
		}
		
		public Builder configFile(String configFile) {
			this.configFile = configFile;
			return this;
		}
		
		public Builder settings(EzySettings settings) {
			this.settings = settings;
			return this;
		}
		
		@Override
		public EzyEmbeddedServer build() {
			if(settings == null)
				throw new IllegalStateException("settings can not be null");
			return new EzyEmbeddedServer(this);
		}
		
	}
	
}

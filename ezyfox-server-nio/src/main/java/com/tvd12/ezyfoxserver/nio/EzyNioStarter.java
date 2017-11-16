package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyNioServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyNioStarter extends EzyStarter {

	protected EzyNioStarter(Builder builder) {
		super(builder);
	}
	
	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new EzyNioServerBootstrapBuilderImpl();
	}
	
	@Override
	protected EzySimpleSessionManager.Builder<?> 
			newSessionManagerBuilder(EzySettings settings) {
		return EzyNioSessionManagerImpl.builder();
	}
	
	
	public static Builder builder() {
    	return new Builder();
    }
    
    public static class Builder extends EzyStarter.Builder<Builder> {
    	@Override
    	public EzyStarter build() {
    		return new EzyNioStarter(this);
    	}
    }
	
}

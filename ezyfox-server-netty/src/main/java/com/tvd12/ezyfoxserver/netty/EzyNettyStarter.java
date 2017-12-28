package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.netty.builder.impl.EzyNettyServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.netty.wrapper.impl.EzyNettySessionManagerImpl;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyNettyStarter extends EzyStarter {
	
	protected EzyNettyStarter(Builder builder) {
		super(builder);
	}
	
	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new EzyNettyServerBootstrapBuilderImpl();
	}
	
	@Override
	protected EzySimpleSessionManager.Builder<?> 
			newSessionManagerBuilder(EzySettings settings) {
		return EzyNettySessionManagerImpl.builder();
	}
	
    public static Builder builder() {
    	return new Builder();
    }
    
    public static class Builder extends EzyStarter.Builder<Builder> {
	    	@Override
	    	public EzyStarter build() {
	    		return new EzyNettyStarter(this);
	    	}
    }
	
}

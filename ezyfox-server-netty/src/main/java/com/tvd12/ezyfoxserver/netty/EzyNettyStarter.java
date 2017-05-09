package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.EzyLoader;
import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.netty.builder.impl.EzyNettyServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.netty.wrapper.impl.EzyNettySessionManagerImpl;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyNettyStarter extends EzyStarter {
	
	protected EzyNettyStarter(Builder builder) {
		super(builder);
	}
	
	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new EzyNettyServerBootstrapBuilderImpl();
	}
	
	@Override
	protected EzyLoader newLoader() {
		return new EzyLoader() {
			@Override
			protected void addManagers(EzyManagers managers) {
				managers.addManager(EzySessionManager.class, EzyNettySessionManagerImpl.builder().build());
			}
		};
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

package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.builder.impl.EzyNettyServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyNettySessionManagerImpl;

public class EzyNettyStarter extends EzyStarter {
	
	protected EzyNettyStarter(Builder builder) {
		super(builder);
	}
	
	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new EzyNettyServerBootstrapBuilderImpl();
	}
	
	@Override
	public EzyManagers newManagers() {
		EzyManagers mngs = super.newManagers();
		mngs.addManager(EzySessionManager.class, EzyNettySessionManagerImpl.builder().build());
		return mngs;
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

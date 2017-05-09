package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyLoader;
import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyNioServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyNioStarter extends EzyStarter {

	protected EzyNioStarter(Builder builder) {
		super(builder);
	}
	
	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new EzyNioServerBootstrapBuilderImpl();
	}
	
	@Override
	protected EzyLoader newLoader() {
		return new EzyLoader() {
			@Override
			protected void addManagers(EzyManagers managers) {
				managers.addManager(EzySessionManager.class, EzyNioSessionManagerImpl.builder().build());
			}
		};
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

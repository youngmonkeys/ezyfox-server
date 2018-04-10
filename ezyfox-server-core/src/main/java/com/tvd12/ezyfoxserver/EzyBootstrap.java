package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyBootstrap 
        extends EzyLoggable 
        implements EzyStartable, EzyDestroyable {

	protected final EzyServerContext context;

	protected EzyBootstrap(Builder builder) {
	    this.context = builder.context;
    }
	
	@Override
	public void start() throws Exception {
		startAllZones();
		startSessionManager();
	}
	
    @Override
	public void destroy() {
	    // do nothing
	}
	
    private void startAllZones() {
        EzyZonesStarter.Builder builder = newZonesStarterBuilder()
                .serverContext(context);
        EzyZonesStarter starter = builder.build();
        starter.start();
    }
    
    protected EzyZonesStarter.Builder newZonesStarterBuilder() {
        return new EzyZonesStarter.Builder();
    }
	
	@SuppressWarnings("rawtypes")
    protected void startSessionManager() throws Exception {
		EzySessionManager sessionManager 
		        = context.getServer().getSessionManager();
		((EzyStartable)sessionManager).start();
	}
	
	protected EzyServer getServer() {
		return context.getServer();
	}
	
	public static Builder builder() {
	    return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyBootstrap> {
	    protected EzyServerContext context;
	    
	    public Builder context(EzyServerContext context) {
	        this.context = context;
	        return this;
	    }
	    
	    @Override
	    public EzyBootstrap build() {
	        return new EzyBootstrap(this);
	    }
	}
}

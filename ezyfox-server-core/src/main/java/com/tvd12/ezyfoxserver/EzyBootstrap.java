package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyServerInitializingEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;
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
	    notifyServerInitializing();
		startAllZones();
		startSessionManager();
	}
	
	protected void notifyServerInitializing() {
	    EzyServerInitializingEvent event = new EzySimpleServerInitializingEvent();
	    context.handleEvent(EzyEventType.SERVER_INITIALIZING, event);
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

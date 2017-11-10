package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;

public class EzyBootstrap 
        extends EzyLoggable 
        implements EzyStartable, EzyDestroyable {

	protected final EzyServerContext context;

	protected EzyBootstrap(Builder builder) {
	    this.context = builder.context;
    }
	
	@Override
	public void start() throws Exception {
		startAllPlugins();
		startAllApps();
		startManagers();
	}
	
	@Override
	public void destroy() {
	    // TODO
	}
	
	protected void startManagers() {
		getManagers().startManagers();
	}
	
	//====================== apps ===================
	protected void startAllApps() {
	    getLogger().info("start all apps ...");
	    startComponent(newAppsStaterBuilder());
	}
	
	protected EzyAppsStarter.Builder newAppsStaterBuilder() {
	    return EzyAppsStarter.builder()
                .appClassLoaders(getServer().getAppClassLoaders());
	}
	//=================================================
	
	//===================== plugins ===================
	protected void startAllPlugins() {
	    getLogger().info("start all plugins ...");
	    startComponent(newPluginsStaterBuilder());
	}
	
	protected EzyPluginsStarter.Builder newPluginsStaterBuilder() {
	    return EzyPluginsStarter.builder();
	}
	
	//=================================================
	
	protected void startComponent(
	        EzyComponentStater.Builder<?, ?> builder) {
	    EzyComponentStater starter = newComponentStater(builder);
	    starter.start();
	}
	
	protected EzyComponentStater newComponentStater(
	        EzyComponentStater.Builder<?, ?> builder) {
	    return builder
	            .serverContext(context)
	            .settings(getServer().getSettings())
	            .build();
	}
	
	protected EzyServer getServer() {
		return context.getServer();
	}
	
	protected EzyManagers getManagers() {
		return getServer().getManagers();
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

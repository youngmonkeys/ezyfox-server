package com.tvd12.ezyfoxserver.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;

@SuppressWarnings("unchecked")
public class EzySimpleServerContextBuilder<B extends EzySimpleServerContextBuilder<B>> 
        implements EzyServerContextBuilder<B> {

    protected EzyServer server;
    
    @Override
    public B server(EzyServer server) {
        this.server = server;
        return (B)this;
    }
    
    @Override
    public EzyServerContext build() {
        EzySimpleServerContext context = newServerContext();
        context.setServer(server);
        context.setExecutorService(newExecutorService());
        context.addAppContexts(newAppContexts(context));
        context.addPluginContexts(newPluginContexts(context));
        return context;
    }
    
    protected EzySimpleServerContext newServerContext() {
        return new EzySimpleServerContext();
    }
    
    protected ScheduledExecutorService newExecutorService() {
        String threadName = "server-thread";
        int nthreads = server.getSettings().getThreadPoolSize();
        if(nthreads > 0)
            return EzyExecutors.newScheduledThreadPool(nthreads, threadName);
        return EzyExecutors.newErrorScheduledExecutor("must set server's 'thread-pool-size'");
    }
    
    protected Collection<EzyAppContext> newAppContexts(EzyServerContext parent) {
        Collection<EzyAppContext> contexts = new ArrayList<>();
        for(Integer appId : server.getAppIds())
            contexts.add(newAppContext(parent, server.getAppById(appId)));
        return contexts;
    }
    
    protected EzyAppContext newAppContext(EzyServerContext parent, EzyAppSetting setting) {
        EzySimpleApplication app = new EzySimpleApplication();
        app.setSetting(setting);
        app.setUserManager(newAppUserManager(setting));
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        appContext.setApp(app);
        appContext.setParent(parent);
        appContext.setExecutorService(newAppExecutorService(setting));
        return appContext;
    }
    
    protected EzyAppUserManager newAppUserManager(EzyAppSetting setting) {
        return EzyAppUserManagerImpl.builder()
                .appName(setting.getName())
                .maxUsers(setting.getMaxUsers())
                .build();
    }
    
    protected Collection<EzyPluginContext> newPluginContexts(EzyServerContext parent) {
        Collection<EzyPluginContext> contexts = new ArrayList<>();
        for(Integer appId : server.getPluginIds())
            contexts.add(newPluginContext(parent, server.getPluginById(appId)));
        return contexts;
    }
    
    protected EzyPluginContext newPluginContext(EzyServerContext parent, EzyPluginSetting setting) {
        EzySimplePlugin plugin = new EzySimplePlugin();
        plugin.setSetting(setting);
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        pluginContext.setPlugin(plugin);
        pluginContext.setParent(parent);
        pluginContext.setExecutorService(newPluginExecutorService(setting));
        return pluginContext;
    }
    
    protected ScheduledExecutorService newAppExecutorService(EzyAppSetting app) {
        String threadName = "app-" + app.getName() + "-thread";
        int nthreads = app.getThreadPoolSize();
        if(nthreads > 0)
            return EzyExecutors.newScheduledThreadPool(nthreads, threadName);
        return EzyExecutors.newErrorScheduledExecutor("must set app " + app.getName() + "'s 'thread-pool-size'");
    }
    
    protected ScheduledExecutorService newPluginExecutorService(EzyPluginSetting plugin) {
        String threadName = "plugin-" + plugin.getName() + "-thread";
        int nthreads = plugin.getThreadPoolSize();
        if(nthreads > 0)
            return EzyExecutors.newScheduledThreadPool(nthreads, threadName);
        return EzyExecutors.newErrorScheduledExecutor("must set plugin " + plugin.getName() + "'s 'thread-pool-size'");
    }
    
}

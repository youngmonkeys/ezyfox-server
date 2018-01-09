package com.tvd12.ezyfoxserver.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

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
        context.setWorkerExecutor(newWorkerExecutor());
        context.addAppContexts(newAppContexts(context));
        context.addPluginContexts(newPluginContexts(context));
        return context;
    }
    
    protected EzySimpleServerContext newServerContext() {
        return new EzySimpleServerContext();
    }
    
    protected ExecutorService newWorkerExecutor() {
        String threadName = "worker";
        int nthreads = server.getSettings().getWorkerPoolSize();
        return EzyExecutors.newFixedThreadPool(nthreads, threadName);
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
        appContext.setWorkerExecutor(newAppWorkerExecutor(setting));
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
        pluginContext.setWorkerExecutor(newPluginWorkerExecutor(setting));
        return pluginContext;
    }
    
    protected ExecutorService newAppWorkerExecutor(EzyAppSetting app) {
        String threadName = "app-worker";
        int nthreads = app.getWorkerPoolSize();
        return EzyExecutors.newFixedThreadPool(nthreads, threadName);
    }
    
    protected ExecutorService newPluginWorkerExecutor(EzyPluginSetting plugin) {
        String threadName = "plugin-worker";
        int nthreads = plugin.getWorkerPoolSize();
        return EzyExecutors.newFixedThreadPool(nthreads, threadName);
    }
    
}

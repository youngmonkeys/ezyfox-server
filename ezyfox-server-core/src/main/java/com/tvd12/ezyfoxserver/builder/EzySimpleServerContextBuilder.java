package com.tvd12.ezyfoxserver.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySimpleUserDelegate;
import com.tvd12.ezyfoxserver.delegate.EzyUserDelegate;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import static com.tvd12.ezyfox.util.EzyProcessor.*;

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
        context.addZoneContexts(newZoneContexts(context));
        return context;
    }
    
    protected EzySimpleServerContext newServerContext() {
        return new EzySimpleServerContext();
    }
    
    protected Collection<EzyZoneContext> newZoneContexts(EzyServerContext parent) {
        Collection<EzyZoneContext> contexts = new ArrayList<>();
        EzySettings settings = EzyServerContexts.getSettings(parent);
        for(Integer zoneId : settings.getZoneIds()) {
            EzyZoneSetting zoneSetting = settings.getZoneById(zoneId);
            EzySimpleZone zone = new EzySimpleZone();
            zone.setSetting(zoneSetting);
            EzyUserDelegate userDelegate = newUserDelegate(parent);
            EzyZoneUserManager userManager = newZoneUserManager(zoneSetting, userDelegate);
            zone.setUserManager(userManager);
            EzySimpleZoneContext zoneContext = new EzySimpleZoneContext();
            zoneContext.setParent(parent);
            zoneContext.setZone(zone);
            zoneContext.addAppContexts(newAppContexts(zoneContext));
            zoneContext.addPluginContexts(newPluginContexts(zoneContext));
            contexts.add(zoneContext);
            processWithException(((EzyStartable)userManager)::start);
        }
        return contexts;
    }
    
    protected EzyUserDelegate newUserDelegate(EzyServerContext context) {
        return new EzySimpleUserDelegate(context);
    }
    
    protected EzyZoneUserManager newZoneUserManager(
            EzyZoneSetting zoneSetting, EzyUserDelegate userDelegate) {
        EzyUserManagementSetting setting = zoneSetting.getUserManagement();
        return EzyZoneUserManagerImpl.builder()
                .userDelegate(userDelegate)
                .zoneName(zoneSetting.getName())
                .maxUsers(zoneSetting.getMaxUsers())
                .maxIdleTime(setting.getUserMaxIdleTime())
                .build();
    }
    
    protected Collection<EzyAppContext> newAppContexts(EzyZoneContext parent) {
        EzyZone zone = parent.getZone();
        EzyZoneSetting zoneSetting = zone.getSetting();
        Collection<EzyAppContext> contexts = new ArrayList<>();
        for(Integer appId : zoneSetting.getAppIds())
            contexts.add(newAppContext(parent, zoneSetting.getAppById(appId)));
        return contexts;
    }
    
    protected EzyAppContext newAppContext(EzyZoneContext parent, EzyAppSetting setting) {
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
    
    protected Collection<EzyPluginContext> newPluginContexts(EzySimpleZoneContext parent) {
        EzyZone zone = parent.getZone();
        EzyZoneSetting zoneSetting = zone.getSetting();
        Collection<EzyPluginContext> contexts = new ArrayList<>();
        for(Integer appId : zoneSetting.getPluginIds())
            contexts.add(newPluginContext(parent, zoneSetting.getPluginById(appId)));
        return contexts;
    }
    
    protected EzyPluginContext newPluginContext(EzySimpleZoneContext parent, EzyPluginSetting setting) {
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

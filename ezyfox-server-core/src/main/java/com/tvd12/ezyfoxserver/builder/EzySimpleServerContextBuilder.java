package com.tvd12.ezyfoxserver.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventPluginsMapperImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;

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
            zone.setUserManager(newZoneUserManager(zoneSetting));
            zone.setEventPluginsMapper(newEventPluginsMapper(zoneSetting));
            EzySimpleZoneContext zoneContext = new EzySimpleZoneContext();
            zoneContext.setParent(parent);
            zoneContext.setZone(zone);
            zoneContext.addAppContexts(newAppContexts(zoneContext));
            zoneContext.addPluginContexts(newPluginContexts(zoneContext));
            contexts.add(zoneContext);
        }
        return contexts;
    }
    
    protected EzyZoneUserManager newZoneUserManager(EzyZoneSetting zoneSetting) {
        EzyUserManagementSetting setting = zoneSetting.getUserManagement();
        return EzyZoneUserManagerImpl.builder()
                .maxUsers(setting.getMaxSessionPerUser())
                .maxIdleTime(setting.getUserMaxIdleTime())
                .build();
    }
    
    protected EzyEventPluginsMapper newEventPluginsMapper(EzyZoneSetting zoneSetting) {
        return EzyEventPluginsMapperImpl.builder()
                .plugins(zoneSetting.getPlugins()).build();
    }
    
    protected Collection<EzyAppContext> newAppContexts(EzyZoneContext parent) {
        EzyZone zone = parent.getZone();
        Collection<EzyAppContext> contexts = new ArrayList<>();
        for(Integer appId : zone.getAppIds())
            contexts.add(newAppContext(parent, zone.getAppById(appId)));
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
        Collection<EzyPluginContext> contexts = new ArrayList<>();
        for(Integer appId : zone.getPluginIds())
            contexts.add(newPluginContext(parent, zone.getPluginById(appId)));
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

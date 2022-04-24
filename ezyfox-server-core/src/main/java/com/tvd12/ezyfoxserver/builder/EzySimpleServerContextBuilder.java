package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.*;
import com.tvd12.ezyfoxserver.context.*;
import com.tvd12.ezyfoxserver.delegate.EzyAppUserDelegate;
import com.tvd12.ezyfoxserver.delegate.EzySimpleAppUserDelegate;
import com.tvd12.ezyfoxserver.delegate.EzySimpleUserDelegate;
import com.tvd12.ezyfoxserver.delegate.EzyUserDelegate;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketUserRemovalQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalQueue;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithException;

@SuppressWarnings("unchecked")
public class EzySimpleServerContextBuilder<B extends EzySimpleServerContextBuilder<B>>
    implements EzyServerContextBuilder<B> {

    protected EzyServer server;
    protected EzySocketUserRemovalQueue socketUserRemovalQueue;

    {
        socketUserRemovalQueue = new EzyBlockingSocketUserRemovalQueue();
    }

    @Override
    public B server(EzyServer server) {
        this.server = server;
        return (B) this;
    }

    @Override
    public EzyServerContext build() {
        EzySimpleServerContext context = newServerContext();
        context.setServer(server);
        context.addZoneContexts(newZoneContexts(context));
        context.setProperty(EzySocketUserRemovalQueue.class, socketUserRemovalQueue);
        context.init();
        return context;
    }

    protected EzySimpleServerContext newServerContext() {
        return new EzySimpleServerContext();
    }

    protected Collection<EzyZoneContext> newZoneContexts(EzyServerContext parent) {
        Collection<EzyZoneContext> contexts = new ArrayList<>();
        EzySettings settings = EzyServerContexts.getSettings(parent);
        for (Integer zoneId : settings.getZoneIds()) {
            EzyZoneSetting zoneSetting = settings.getZoneById(zoneId);
            EzySimpleZone zone = new EzySimpleZone();
            zone.setSetting(zoneSetting);
            EzyUserDelegate userDelegate = newUserDelegate(parent);
            EzyZoneUserManager userManager = newZoneUserManager(zoneSetting, userDelegate);
            EzyEventControllers eventControllers = newEventControllers(zoneSetting.getEventControllers());
            zone.setUserManager(userManager);
            zone.setEventControllers(eventControllers);
            EzySimpleZoneContext zoneContext = new EzySimpleZoneContext();
            zoneContext.setParent(parent);
            zoneContext.setZone(zone);
            zoneContext.addAppContexts(newAppContexts(zoneContext));
            zoneContext.addPluginContexts(newPluginContexts(zoneContext));
            zoneContext.init();
            contexts.add(zoneContext);
            processWithException(((EzyStartable) userManager)::start);
        }
        return contexts;
    }

    protected EzyUserDelegate newUserDelegate(EzyServerContext context) {
        return new EzySimpleUserDelegate(context, socketUserRemovalQueue);
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

    protected EzyEventControllers newEventControllers() {
        return new EzyEventControllersImpl();
    }

    protected EzyEventControllers newEventControllers(EzyEventControllersSetting setting) {
        return EzyEventControllersImpl.create(setting);
    }

    protected Collection<EzyAppContext> newAppContexts(EzyZoneContext parent) {
        EzyZone zone = parent.getZone();
        EzyZoneSetting zoneSetting = zone.getSetting();
        Collection<EzyAppContext> contexts = new ArrayList<>();
        for (Integer appId : zoneSetting.getAppIds()) {
            contexts.add(newAppContext(parent, zoneSetting.getAppById(appId)));
        }
        return contexts;
    }

    protected EzyAppContext newAppContext(EzyZoneContext parent, EzyAppSetting setting) {
        EzySimpleAppUserDelegate userDelegate = new EzySimpleAppUserDelegate();
        EzyAppUserManager appUserManager = newAppUserManager(setting, userDelegate);
        EzyEventControllers eventControllers = newEventControllers();
        EzySimpleApplication app = new EzySimpleApplication();
        app.setSetting(setting);
        app.setUserManager(appUserManager);
        app.setEventControllers(eventControllers);
        ScheduledExecutorService appExecutorService = newAppExecutorService(setting);
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        userDelegate.setAppContext(appContext);
        appContext.setApp(app);
        appContext.setParent(parent);
        appContext.setExecutorService(appExecutorService);
        appContext.init();
        return appContext;
    }

    protected EzyAppUserManager
    newAppUserManager(EzyAppSetting setting, EzyAppUserDelegate userDelegate) {
        return EzyAppUserManagerImpl.builder()
            .appName(setting.getName())
            .maxUsers(setting.getMaxUsers())
            .userDelegate(userDelegate)
            .build();
    }

    protected Collection<EzyPluginContext> newPluginContexts(EzySimpleZoneContext parent) {
        EzyZone zone = parent.getZone();
        EzyZoneSetting zoneSetting = zone.getSetting();
        Collection<EzyPluginContext> contexts = new ArrayList<>();
        for (Integer appId : zoneSetting.getPluginIds()) {
            contexts.add(newPluginContext(parent, zoneSetting.getPluginById(appId)));
        }
        return contexts;
    }

    protected EzyPluginContext newPluginContext(EzySimpleZoneContext parent, EzyPluginSetting setting) {
        EzySimplePlugin plugin = new EzySimplePlugin();
        plugin.setSetting(setting);
        plugin.setEventControllers(newEventControllers());
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        pluginContext.setPlugin(plugin);
        pluginContext.setParent(parent);
        pluginContext.setExecutorService(newPluginExecutorService(setting));
        pluginContext.init();
        return pluginContext;
    }

    protected ScheduledExecutorService newAppExecutorService(EzyAppSetting app) {
        String threadName = "app-" + app.getName() + "-thread";
        int nthreads = app.getThreadPoolSize();
        if (nthreads > 0) {
            return EzyExecutors.newScheduledThreadPool(nthreads, threadName);
        }
        return EzyExecutors.newErrorScheduledExecutor("must set app " + app.getName() + "'s 'thread-pool-size'");
    }

    protected ScheduledExecutorService newPluginExecutorService(EzyPluginSetting plugin) {
        String threadName = "plugin-" + plugin.getName() + "-thread";
        int nthreads = plugin.getThreadPoolSize();
        if (nthreads > 0) {
            return EzyExecutors.newScheduledThreadPool(nthreads, threadName);
        }
        return EzyExecutors.newErrorScheduledExecutor("must set plugin " + plugin.getName() + "'s 'thread-pool-size'");
    }

}
